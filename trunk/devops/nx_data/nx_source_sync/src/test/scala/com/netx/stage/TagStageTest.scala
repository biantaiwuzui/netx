package com.netx.stage

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Keep }
import akka.stream.testkit.scaladsl.{ TestSink, TestSource }
import akka.testkit.{ ImplicitSender, TestKit }
import com.netx.stages.{ StageCoordinator, TagStage }
import org.scalatest.{ BeforeAndAfterAll, FunSuiteLike, Matchers }

class TagStageTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with FunSuiteLike with Matchers with BeforeAndAfterAll {
  val TestMessage = "akka is a toolkit and runtime for building highly concurrent, distributed, and resilient message-driven applications on the JVM"
  implicit val materializer = ActorMaterializer()

  def this() = this(ActorSystem("tag-stage-test"))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  test("Dynamic stage") {
    // Create the tag stage with testActor as the coordinator actor
    val tagStage = Flow.fromGraph(new TagStage(testActor, Map("akka" -> Set("scala", "actors"))))

    // Create the flow
    val (src, snk) = TestSource.probe[String]
      .via(tagStage)
      .toMat(TestSink.probe)(Keep.both)
      .run()

    // expect registration and subscription start messages
    val stageActorRegistration = expectMsgType[StageCoordinator.Register]
    val subscription = snk.expectSubscription()

    // Send a message through the pipeline
    subscription.request(1)
    src.sendNext(TestMessage)

    val tagResult1 = snk.expectNext()
    tagResult1 should have size 2
    tagResult1 should contain("scala")
    tagResult1 should contain("actors")

    // Update the keyword tag set through the stage actor
    stageActorRegistration.actorRef ! StageCoordinator.AddKeywordTags("akka", Set("jvm", "message-driven"))

    // Send the message through the pipeline again
    subscription.request(1)
    src.sendNext(TestMessage)

    val tagResult2 = snk.expectNext()
    tagResult2 should have size 4
    tagResult2 should contain("scala")
    tagResult2 should contain("actors")
    tagResult2 should contain("jvm")
    tagResult2 should contain("message-driven")

    // Remove the keyword through the stage actor
    stageActorRegistration.actorRef ! StageCoordinator.RemoveKeyword("akka")

    // Send the message through the pipeline again
    subscription.request(1)
    src.sendNext(TestMessage)

    // Since there are no keywords to match, nothing should come out
    snk.expectNoMsg()

    // Shutdown the flow
    src.sendComplete()
    snk.expectComplete()
  }
}