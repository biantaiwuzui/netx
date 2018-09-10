package com.netx

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Sink, Source }
import com.netx.SyncApp.getClass
import com.netx.stages.AccumulateWhileUnchanged

import scala.collection.immutable
import scala.concurrent.Future

object TestApp extends App {
  implicit val system = ActorSystem("Netx-Sync")
  implicit val executor = system.dispatcher
  implicit val log = Logging(system, getClass)
  implicit val materializer = ActorMaterializer()

  //  Source(1 to 2)
  //    .mapAsync(1)(spin)
  //    .runWith(Sink.ignore)
  //
  //  def spin(value: Int): Future[Int] = Future {
  //    val start = System.currentTimeMillis()
  //    while ((System.currentTimeMillis() - start) < 10) {}
  //    println(value)
  //    value
  //  }

  Source(SampleElements.All)
    .via(new AccumulateWhileUnchanged(_.value))
    .runWith(Sink.foreach(println))
}
case class Element(id: Int, value: Int)

object SampleElements {

  val E11 = Element(1, 1)
  val E21 = Element(2, 1)
  val E31 = Element(3, 1)
  val E42 = Element(4, 2)
  val E52 = Element(5, 2)
  val E63 = Element(6, 3)

  val Ones = immutable.Seq(E11, E31)
  val Twos = immutable.Seq(E42, E52)
  val Threes = immutable.Seq(E63)
  val Four = immutable.Seq(E21)

  val All = Ones ++ Twos ++ Threes ++ Four
}
