package com.lightbend.akkassembly

import akka.stream.Materializer
import akka.stream.scaladsl.GraphDSL.Implicits.SourceArrow
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future

class Factory(bodyShop: BodyShop, paintShop: PaintShop, engineShop: EngineShop, wheelShop: WheelShop,
              qualityAssurance: QualityAssurance, upgradeShop: UpgradeShop)(implicit materializer: Materializer) {

  def orderCars(quantity: Int): Future[Seq[Car]] =
    bodyShop.cars
      .via(paintShop.paint.named("paint-stage")) // 6ms
      .async
      .via(engineShop.installEngine.named("install-engine-stage")) // 6.4ms
      .async
      .via(wheelShop.installWheels.named("install-wheels-stage")) // 5ms
      .async
      .via(upgradeShop.installUpgrades.named("install-upgrades-stage")) // 4ms
      .via(qualityAssurance.inspect.named("inspect-stage"))
      .via(Flow[Car].take(quantity))
      .runWith(Sink.collection)

}
