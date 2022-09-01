package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}

class EngineShop(shipmentSize: Int) {
  val shipments: Source[Shipment, NotUsed] =
    Source.fromIterator(() => Iterator.continually(Shipment(Seq.fill(shipmentSize)(Engine()))))

  val engines: Source[Engine, NotUsed] = shipments.mapConcat(s => s.engines)

  val installEngine: Flow[UnfinishedCar, UnfinishedCar, NotUsed] =
    Flow[UnfinishedCar].zip(engines).map(c => c._1.installEngine(c._2))
}
