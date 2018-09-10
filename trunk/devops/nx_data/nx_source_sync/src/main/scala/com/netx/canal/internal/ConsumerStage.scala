package com.netx.canal.internal

import akka.Done
import akka.actor.{ActorLogging, ActorRef, ExtendedActorSystem, Terminated}
import akka.stream._
import akka.stream.stage.GraphStageLogic.StageActor
import akka.stream.stage.{GraphStageLogic, GraphStageWithMaterializedValue, OutHandler}
import com.alibaba.otter.canal.protocol.CanalEntry.Entry
import com.netx.canal.internal.CanalConsumerActor.Internal._
import com.typesafe.config.Config

import scala.annotation.tailrec
import scala.concurrent.{Future, Promise}

trait Control {
  def stop(): Future[Done]
  def shutdown(): Future[Done]
  def isShutdown: Future[Done]
}

abstract class CanalSourceStage[M] extends GraphStageWithMaterializedValue[SourceShape[M], Control] {
  protected val out = Outlet[M]("canal.out")
  val shape = new SourceShape(out)

  protected def logic(shape: SourceShape[M]): GraphStageLogic with Control

  override def createLogicAndMaterializedValue(inheritedAttributes: Attributes): (GraphStageLogic, Control) = {
    val result = logic(shape)
    (result, result)
  }
}

private[canal] class CanalSourceLogic(val shape: SourceShape[Messages], config: Config)
  extends GraphStageLogic(shape) with PromiseControl {
  var consumer: ActorRef = _
  var self: StageActor = _
  var buffer: Iterator[Messages] = Iterator.empty
  var requestId = 0
  var needRequestMessage = true

  override def preStart(): Unit = {
    super.preStart()

    consumer = {
      val extendedActorSystem = downcast(materializer).system.asInstanceOf[ExtendedActorSystem]
      val name = s"canal-consumer-${CanalConsumerActor.Internal.nextNumber()}"
      extendedActorSystem.systemActorOf(CanalConsumerActor.props(config), name)
    }
    self = getStageActor {
      case (_, msg: Messages) =>
        if (buffer.hasNext)
          buffer = buffer ++ Iterator(msg)
        else
          buffer = Iterator(msg)
        pump()
      case (_, ReplyAckSuccessed) =>
        requestId += 1
        needRequestMessage = true
        pump()
      case (_, Terminated(ref)) if ref == consumer =>
        failStage(new RuntimeException("consumer failed"))
    }
    self.watch(consumer)

    consumer ! Subscribe
  }

  private def pump(): Unit = {
    if (isAvailable(shape.out) && buffer.hasNext) {
        val msg = buffer.next()
        push(shape.out, msg)
        consumer.tell(ReplyAck(msg.batchId), self.ref)
    } else if(needRequestMessage) {
      requestId += 1
      needRequestMessage = false
      consumer.tell(RequestMessage(requestId), self.ref)
    }
  }

  setHandler(shape.out, new OutHandler {
    override def onPull(): Unit = {
      pump()
    }

    override def onDownstreamFinish(): Unit = {
      performShutdown()
    }
  })

  override def performShutdown(): Unit = {
    setKeepGoing(true)
    if (!isClosed(shape.out)) {
      complete(shape.out)
    }
    self.become {
      case (_, Terminated(ref)) if ref == consumer =>
        onShutdown()
        completeStage()
    }
    consumer ! Stop
  }

  override def postStop(): Unit = {
    consumer ! Stop
    onShutdown()
    super.postStop()
  }

  private def downcast(materializer: Materializer): ActorMaterializer =
    materializer match {
      case m: ActorMaterializer => m
      case _ ⇒ throw new IllegalArgumentException(s"required [${classOf[ActorMaterializer].getName}] " +
        s"but got [${materializer.getClass.getName}]")
    }
}

private[canal] sealed trait ControlOperation

private[canal] case object ControlStop extends ControlOperation

private[canal] case object ControlShutdown extends ControlOperation

private[canal] trait PromiseControl extends GraphStageLogic with Control {
  def shape: SourceShape[_]
  def performShutdown(): Unit
  def performStop(): Unit = {
    setKeepGoing(true)
    complete(shape.out)
    onStop()
  }

  private val shutdownPromise: Promise[Done] = Promise()
  private val stopPromise: Promise[Done] = Promise()

  private val controlCallback = getAsyncCallback[ControlOperation]({
    case ControlStop => performStop()
    case ControlShutdown => performShutdown()
  })

  def onStop() = {
    stopPromise.trySuccess(Done)
  }

  def onShutdown() = {
    stopPromise.trySuccess(Done)
    shutdownPromise.trySuccess(Done)
  }

  override def stop(): Future[Done] = {
    controlCallback.invoke(ControlStop)
    stopPromise.future
  }
  override def shutdown(): Future[Done] = {
    controlCallback.invoke(ControlShutdown)
    shutdownPromise.future
  }
  override def isShutdown: Future[Done] = shutdownPromise.future
}
