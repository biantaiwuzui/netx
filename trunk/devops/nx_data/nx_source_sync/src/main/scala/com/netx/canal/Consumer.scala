package com.netx.canal

import akka.stream.SourceShape
import akka.stream.scaladsl.Source
import akka.stream.stage.GraphStageLogic
import com.netx.canal.internal.CanalConsumerActor.Internal.Messages
import com.netx.canal.internal.{ CanalSourceLogic, CanalSourceStage, Control }
import com.typesafe.config.Config

object CanalConsumer {
  def plainSource(config: Config): Source[Messages, Control] =
    Source.fromGraph(canalSourceStage(config))

  def canalSourceStage(config: Config) = {
    new CanalSourceStage[Messages] {
      override protected def logic(shape: SourceShape[Messages]): GraphStageLogic with Control = {
        new CanalSourceLogic(shape, config)
      }
    }
  }
}
