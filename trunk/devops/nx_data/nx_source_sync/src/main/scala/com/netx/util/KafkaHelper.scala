package com.netx.util

import akka.actor.ActorSystem
import akka.kafka.{ ConsumerSettings, ProducerSettings }
import io.circe.{ Decoder, Encoder }
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ StringDeserializer, StringSerializer }
import com.ovoenergy.kafka.serialization.circe.{ circeJsonDeserializer, circeJsonSerializer }
import com.typesafe.config.Config

trait KafkaHelper {
  def producerSettings[T: Encoder](system: ActorSystem, config: Config) =
    ProducerSettings(system, new StringSerializer, circeJsonSerializer[T])
      .withParallelism(1800)
      .withBootstrapServers(config.getString("servers"))

  def consumerSettings[T: Decoder](system: ActorSystem, config: Config) = ConsumerSettings(system, new StringDeserializer, circeJsonDeserializer[T])
    .withBootstrapServers(config.getString("servers"))
    .withGroupId(config.getString("group-id"))
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
}
