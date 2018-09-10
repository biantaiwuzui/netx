package com.netx.actors

import java.util.Date

import akka.actor.{ Actor, ActorLogging, Props, Timers }
import akka.kafka.scaladsl.Producer
import akka.stream.{ Attributes, Materializer, OverflowStrategy, QueueOfferResult }
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.typesafe.config.Config
import akka.util.Timeout
import com.alibaba.otter.canal.protocol.CanalEntry._
import com.alibaba.otter.canal.protocol.Message
import com.netx.message.SourceChangeMessage
import com.netx.util._
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.duration._
import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto._

import scala.collection.JavaConverters._
import scala.concurrent.Future

sealed trait CanalConnectionInfo
case class SimpleConnectionInfo(val ip: String, val port: Int, val destination: String, val userName: String = "", val password: String = "") extends CanalConnectionInfo
case class ClusterConnectionInfo(val zkServers: String, val destination: String, val userName: String = "", val password: String = "") extends CanalConnectionInfo

//
//case class UMSBody(protocol: UMSProtocol, schema: UMSSchema, payload: List[UMSMessageRow])
//case class UMSProtocol(typ: String)
//case class UMSSchema(namespace: String, fields: List[UMSFieldSchema])
//case class UMSFieldSchema(filedName: String, filedType: String, nullable: Boolean)
//case class UMSMessageRow(key: String = "row", value: List[(Option[String], Option[String])])

object CanalMessageActor {
  sealed trait CanalMessage
  private case object TickMessage extends CanalMessage
  private case class FetchMessage(batchId: Long, message: Message) extends CanalMessage
  case object TestMessage extends CanalMessage

  private val CANAL_TIMER_KEY = "canal-consumer"
  private val CANAL_TIMER_DURATION = 1.second
  private val CANAL_SOURCE_TOPIC = "CANAL_DATA_SOURCE"

  def props(config: Config)(implicit materialize: Materializer) = Props(new CanalMessageActor(config))

  type SourceChangeMessageRecord = ProducerRecord[String, SourceChangeMessage]
}

class CanalMessageActor(config: Config)(implicit materialize: Materializer)
  extends ConnectorWrapper(config)
  with Actor
  with Timers
  with CanalMessageParser
  with KafkaHelper
  with ActorLogging {
  import com.netx.actors.CanalMessageActor._
  import SourceChangeMessage._

  implicit val ec = context.dispatcher
  implicit val timeout: Timeout = 1.second

  //build ActorRef receive message, not support backpressure
  //  val recordSource = Source.actorRef[SourceChangeMessage](1, OverflowStrategy.fail)
  //    .map(elem => new SourceChangeMessageRecord(CANAL_SOURCE_TOPIC, elem))

  //Support backpressure
  val kafkaSink = Producer.plainSink(producerSettings[SourceChangeMessage](context.system, config))
  val queue = Source.queue[SourceChangeMessageRecord](10, OverflowStrategy.backpressure)
    .to(kafkaSink).run()
  //  val kafkaSinkTest = Producer.plainSink(producerSettings[String](context.system))

  override def preStart(): Unit = {
    super.connect
    timers.startSingleTimer(CANAL_TIMER_KEY, TickMessage, CANAL_TIMER_DURATION)
    super.preStart()
  }

  override def receive: Receive = {
    case TickMessage => {
      val message: Message = getMessage
      val batchId = message.getId
      val size = message.getEntries.size
      if (batchId == -1 || size == 0) {
        timers.startSingleTimer(CANAL_TIMER_KEY, TickMessage, CANAL_TIMER_DURATION)
        ack(batchId)
      } else {
        self ! FetchMessage(batchId, message)
      }
    }
    case FetchMessage(batchId, message) => {
      Source(message.getEntries.asScala.toList)
        .map(m => parse(Some(m)))
        .mapConcat(identity)
        .mapAsync(1) {
          case scm: SourceChangeMessage => queue.offer(new ProducerRecord(CANAL_SOURCE_TOPIC, scm))
        }
      ack(batchId)
      self ! TickMessage
    }
    //    case TestMessage => {
    //      Source(1 to 100000)
    //        .mapAsync(600)(testMessage)
    //        .buffer(100000, OverflowStrategy.backpressure)
    //        .runWith(kafkaSinkTest)
    //    }
  }

  override def postStop(): Unit = {
    super.postStop()
    disconnect
  }

  def tableNames: List[TableSchema] = List(
    "demand,id,user_id",
    "demand_order,demand_id,user_id",
    "demand_registry,demand_id,user_id",

    "skill,id,user_id",
    "skill_order,skill_register_id,create_user_id",
    "skill_register,skill_id,user_id",

    "meeting,id,user_id",
    "meeting_send,meeting_id,user_id",
    "meeting_register,meeting_id,user_id").map(Creator.create[TableSchema]).flatten
}
