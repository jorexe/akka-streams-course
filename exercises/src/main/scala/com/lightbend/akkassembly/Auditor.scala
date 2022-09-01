package com.lightbend.akkassembly

import akka.{Done, NotUsed}
import akka.event.Logging.DebugLevel
import akka.event.LoggingAdapter
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class Auditor(implicit materializer: Materializer) {
  val count: Sink[Any, Future[Int]] = Sink.fold(0)((c, _) => c + 1)

  def log(implicit loggingAdapter: LoggingAdapter): Sink[Any, Future[Done]] =
    Sink.foreach(e => loggingAdapter.log(DebugLevel, e.toString))

  def sample(sampleSize: FiniteDuration): Flow[Car, Car, NotUsed] =
    Flow[Car].takeWithin(sampleSize)

  def audit(cars: Source[Car, NotUsed], sampleSize: FiniteDuration): Future[Int] =
    cars.via(sample(sampleSize)).toMat(count)(Keep.right).run()
}
