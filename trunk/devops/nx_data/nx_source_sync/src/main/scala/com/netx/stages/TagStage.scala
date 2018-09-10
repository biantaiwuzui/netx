package com.netx.stages

import akka.actor.ActorRef
import akka.stream.{ Attributes, FlowShape, Inlet, Outlet }
import akka.stream.stage.{ GraphStage, GraphStageLogic, InHandler, OutHandler }

import scala.collection.mutable;

class TagStage(stageCoordinator: ActorRef, initialKeywordMap: Map[String, Set[String]]) extends GraphStage[FlowShape[String, Set[String]]] {
  val in: Inlet[String] = Inlet("tag-stage-in")
  val out: Outlet[Set[String]] = Outlet("tag-stage-out")

  override def shape: FlowShape[String, Set[String]] = FlowShape(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    implicit def self = stageActor.ref

    var keywordsMap = mutable.HashMap(initialKeywordMap.toSeq: _*)

    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        val msg = grab(in)

        val tags = msg.split("\\W+").collect {
          case flaggedWord if keywordsMap.contains(flaggedWord) => keywordsMap(flaggedWord)
        }.flatten

        if (tags.isEmpty) {
          tryPull(in)
        } else {
          push(out, Set(tags: _*))
        }
      }
    })

    setHandler(out, new OutHandler {
      override def onPull(): Unit = tryPull(in)
    })

    override def preStart(): Unit = {
      val thisStageActor = getStageActor(messageHandler).ref
      stageCoordinator ! StageCoordinator.Register(thisStageActor)
    }

    private def messageHandler(receive: (ActorRef, Any)): Unit = {
      receive match {
        case (_, StageCoordinator.AddKeywordTags(keyword, tags)) => {
          val newTags = keywordsMap.get(keyword).map(_ ++ tags).getOrElse(tags)
          keywordsMap.update(keyword, newTags)
        }
        case (_, StageCoordinator.RemoveKeyword(keyword)) => {
          keywordsMap.remove(keyword)
        }
      }
    }
  }

}
