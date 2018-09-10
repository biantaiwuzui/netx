package com.netx.stages

import akka.actor.{ Actor, ActorLogging, ActorRef, Props, Terminated }

import scala.collection.mutable

object StageCoordinator {
  final case class Register(actorRef: ActorRef)

  sealed trait TagCommand extends Product with Serializable

  final case class AddKeywordTags(keyword: String, tags: Set[String]) extends TagCommand
  final case class RemoveKeyword(keyword: String) extends TagCommand

  def props = Props[StageCoordinator]
}

class StageCoordinator extends Actor with ActorLogging {
  val stageActors: mutable.Set[ActorRef] = mutable.HashSet.empty

  import StageCoordinator._

  override def receive: Receive = {
    case Register(actorRef) => {
      log.info("Registering stage actor [{}]", actorRef)
      stageActors.add(actorRef)
      context.watch(actorRef)
    }
    case Terminated(actorRef) => {
      log.info("Stage actor terminated [{}]", actorRef)
      stageActors.remove(actorRef)
      context.unwatch(actorRef)
    }
    case cmd: TagCommand => {
      log.info("Received command:[{}]", cmd)
      stageActors.foreach(_ ! cmd)
    }
  }
}
