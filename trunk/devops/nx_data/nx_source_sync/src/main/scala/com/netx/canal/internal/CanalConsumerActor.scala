package com.netx.canal.internal

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable, PoisonPill, Props, Terminated}
import com.alibaba.otter.canal.protocol.CanalEntry.Entry
import com.alibaba.otter.canal.protocol.Message
import com.alibaba.otter.canal.protocol.exception.CanalClientException
import com.netx.util.{ConnectorWrapper, Creator, TableSchema}
import com.typesafe.config.Config
import org.springframework.util.StringUtils

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.util.control.NoStackTrace

object CanalConsumerActor {
  private val BATCH_SIZE = 5 * 1024

  object Internal {
    sealed trait SubscriptionRequest
    final case object Subscribe extends SubscriptionRequest

    final case class RequestMessage(requestId: Int)
    final case class Messages(batchId: Long, entries: List[Entry])
    final case class ReplyAck(batchId: Long)
    final case object ReplyAckSuccessed

    private[CanalConsumerActor] final case class Poll(target: CanalConsumerActor)

    private[CanalConsumerActor] class NoPollResult extends RuntimeException with NoStackTrace

    final case object Stop

    private val number = new AtomicInteger()
    def nextNumber(): Int = {
      number.incrementAndGet()
    }
  }

  def props(config: Config): Props =
    Props(new CanalConsumerActor(config))
}

class CanalConsumerActor(config: Config) extends ConnectorWrapper(config) with Actor with ActorLogging {
  import CanalConsumerActor._
  import CanalConsumerActor.Internal._

  val pollMsg = Poll(this)
  val delayedPollMsg = Poll(this)
  //var canalConnector: CanalConnector = _
  var currentPollTask: Cancellable = _
  def pollTimeout() = 5.seconds
  def pollInterval() = 5.seconds

  var requestActorRef: ActorRef = _
  var subscriptions = Set.empty[SubscriptionRequest]

  override def receive: Receive = {
    case s: SubscriptionRequest =>
      subscriptions = subscriptions + s
      handleSubscription(s)
    case p: Poll =>
      log.info("get msg from poll")
      receivePoll(p)
    case req: RequestMessage =>
      context.watch(sender())
      requestActorRef = sender()
      log.info("request message from stream")
      log.info(s"RequestMessage requestActorRef:${requestActorRef}")
      poll()
    case ReplyAck(batchId) =>
      log.info(s"reply ack batchId:${batchId}")
      requestActorRef = sender()
      log.info(s"ReplayAck requestActorRef:${requestActorRef}")
      tryAck(batchId)
      requestActorRef ! ReplyAckSuccessed
      //currentPollTask = schedulePollTask(100.millisecond)
  }

  def schedulePollTask(interval: FiniteDuration = pollInterval()): Cancellable =
    context.system.scheduler.scheduleOnce(interval, self, pollMsg)(context.dispatcher)

  private def receivePoll(p: Poll): Unit = {
    if (p.target == this) {
      poll()
      //currentPollTask = schedulePollTask()
    } else {
      log.debug("Ignoring Poll message with stale target ref")
    }
  }

  def handleSubscription(subscription: SubscriptionRequest): Unit = {
    subscription match {
      case Subscribe => {
        try {
          connector.connect()
          connector.subscribe()
        } catch { case e: Exception => log.error(e, "connect or subscribe error") }
      }
    }
  }

  def poll(): Unit = {
    val messages = tryPoll()
    messages match {
      case Some(message) => {
        val batchId = message.getId
        log.info(s"get batchId: ${batchId}")
        val entries = message.getEntries.asScala.toList
        if (batchId > -1 || entries.size > 0) {
          entries.foreach { p=>
            if (!StringUtils.isEmpty(p.getHeader.getTableName))
              log.info(p.getHeader.getTableName)
          }
          requestActorRef ! Messages(batchId, entries)
        } else {
          tryAck(batchId)
          currentPollTask = schedulePollTask(5.seconds)
        }
      }
      case None =>
    }
  }

  def tryPoll(): Option[Message] = {
    try {
      Some(connector.getWithoutAck(BATCH_SIZE))
    } catch {
      case c: CanalClientException =>
        log.error(c, "Exception when get from canal")
        // context.stop(self)
        None
      case e: NullPointerException =>
        requestActorRef ! PoisonPill
        log.error(e, "null pointer")
        None
    }
  }
  def tryAck(batchId: Long): Unit = {
    try {
      connector.ack(batchId)
    } catch {
      case c: CanalClientException =>
        log.error(c, "Exception when ack to canal server")
      //  context.stop(self)
    }
  }

}
