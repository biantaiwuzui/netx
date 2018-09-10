package com.netx

import akka.actor.ActorSystem
import akka.event.Logging
import akka.kafka.scaladsl.{ Consumer, Producer }
import akka.stream.scaladsl.{ Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source }
import akka.stream.{ ActorMaterializer, ClosedShape }
import com.alibaba.otter.canal.protocol.CanalEntry.EventType
import com.netx.canal.CanalConsumer
import com.netx.canal.internal.CanalConsumerActor.Internal.Messages
import com.netx.message.SourceChangeMessage
import com.netx.util._
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.Future
import scala.io.StdIn

object SyncApp extends App with StrictLogging with KafkaHelper with CanalMessageParser {
  implicit val system = ActorSystem("Netx-Sync")
  implicit val executor = system.dispatcher
  implicit val log = Logging(system, getClass)
  implicit val materializer = ActorMaterializer()
  val config = ConfigFactory.load()
  val canalConfig = config.getConfig("canal")
  val kakfaConfig = config.getConfig("kafka")

  val runnableGraph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val canalSource = CanalConsumer.plainSource(canalConfig)
    val kafkaSink = Producer.plainSink[String, SourceChangeMessage](producerSettings[SourceChangeMessage](system, kakfaConfig))
    val bcast = builder.add(Broadcast[SourceChangeMessage](2))
    val sourceChangeFlow = Flow[Messages].mapConcat { elem =>
      elem.entries.flatMap(p => parse(Some(p)))
    }

    val producerRecordFlow = Flow[SourceChangeMessage].map(m => new ProducerRecord[String, SourceChangeMessage](kakfaConfig.getString("topic"), m))
    val redisFlow = Flow[SourceChangeMessage].mapAsync(1)(m => Future { logger.info(m.toString) })

    canalSource ~> sourceChangeFlow ~> bcast ~> producerRecordFlow ~> kafkaSink
    bcast ~> redisFlow ~> Sink.ignore
    ClosedShape
  })
  runnableGraph.run()

  StdIn.readLine()

  def tableNames: List[TableSchema] = List(
    "demand,id,user_id",
    "demand_order,demand_id,user_id",
    "demand_registry,demand_id,user_id",

    "skill,id,user_id",
    "skill_order,skill_register_id,create_user_id",
    "skill_register,skill_id,user_id",

    "wish,id,user_id",
    "wish_apply,wish_id,user_id",
    "wish_authorize,wish_id,user_id",
    "wish_bank,wish_id,user_id",
    "wish_manager,wish_id,user_id",
    "wish_referee,wish_id,user_id",
    "wish_support,wish_id,user_id",

    "meeting,id,user_id",
    "meeting_send,meeting_id,user_id",
    "meeting_register,meeting_id,user_id",


    "match_event,id,initiator_id",
    "match_review,match_id,merchant_id").map(Creator.create[TableSchema]).flatten
}
