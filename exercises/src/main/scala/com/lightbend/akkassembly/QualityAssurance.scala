package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.{ActorAttributes, Supervision}
import akka.stream.scaladsl.Flow

object QualityAssurance {
  case class CarFailedInspection(car: UnfinishedCar) extends IllegalStateException(s"Car did not pass the auditory: $car")
}

class QualityAssurance {

  private val decider: Supervision.Decider = {
    case _: QualityAssurance.CarFailedInspection => Supervision.Resume
  }

  val inspect: Flow[UnfinishedCar, Car, NotUsed] = Flow[UnfinishedCar]
    .map {
      case UnfinishedCar(Some(color), Some(engine), wheels, upgrade) if wheels.size == 4 =>
        Car(SerialNumber(), color, engine, wheels, upgrade)
      case unfinishedCar: UnfinishedCar => throw QualityAssurance.CarFailedInspection(unfinishedCar)
    }
    .withAttributes(ActorAttributes.supervisionStrategy(decider))

}
