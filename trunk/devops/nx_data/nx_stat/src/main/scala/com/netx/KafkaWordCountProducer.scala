package com.netx

import java.util.HashMap

import akka.NotUsed
import akka.http.scaladsl.model.headers.CacheDirectives.public
import org.apache.kafka.clients.producer._

import scala.concurrent.Promise

object KafkaWordCountProducer extends App {
  val topic = "sparkstream_topic"
  val messagesPerSec = "10"
  val wordsPerMessage = "20"

  // Zookeeper connection properties
  val props = new HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  props.put(
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put(
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val callback = new Callback {
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
      Option(exception) match {
        case Some(err) => { println(err) }
        case None      => { println("success send") }
      }
    }
  }

  // Send some messages
  while (true) {
    (1 to messagesPerSec.toInt).foreach { messageNum =>
      val str = (1 to wordsPerMessage.toInt).map(x => scala.util.Random.nextInt(10).toString)
        .mkString(" ")

      val message = new ProducerRecord[String, String](topic, null, str)
      producer.send(message, callback)
    }

    Thread.sleep(1000)
  }

}

