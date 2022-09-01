package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.FlowShape
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge}
import com.lightbend.akkassembly.Upgrade.{DX, Sport}

class UpgradeShop {
  val installUpgrades: Flow[UnfinishedCar, UnfinishedCar, NotUsed] =
    Flow.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._

      val balance = builder.add(Balance[UnfinishedCar](3))
      val merge = builder.add(Merge[UnfinishedCar](3))

      val f1 = Flow[UnfinishedCar]
      val f2 = Flow[UnfinishedCar].map(c => c.installUpgrade(DX))
      val f3 = Flow[UnfinishedCar].map(c => c.installUpgrade(Sport))

      balance ~> f1 ~> merge
      balance ~> f2 ~> merge
      balance ~> f3 ~> merge

      FlowShape(balance.in, merge.out)
    })
}
