package com.netx

import io.circe.{Json, Decoder => IDecoder, Encoder => IEncoder}
import kafka.serializer.{Decoder, StringDecoder}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import kafka.utils.VerifiableProperties

object StatApp extends App {
  val conf = new SparkConf().setAppName("stat")
  val ssc = new StreamingContext(conf, Seconds(2))
  //ssc.checkpoint("/statCheckpoint")

  val topics = "kafka_spark"
  val topicMap = topics.split(",").map((_, 2)).toMap
  val kafkaParams: Map[String, String] = Map("group.id" -> "test", "bootstrap.servers" -> "127.0.0.1:9092", "auto.offset.reset" -> "smallest")

  val kafkaMessage = KafkaUtils.createDirectStream[String, OrderProfit, StringDecoder, OrderProfitDecoder](ssc, kafkaParams, Set("kafka_spark"))
  println("hello world spark")

  val words = kafkaMessage.map(_._2)
  words.foreachRDD(rdd => {
    rdd.foreachPartition(p => {
      println("msg:" + p)
    })
  })l
  ssc.start()
  ssc.awaitTermination()
  ssc.stop()
}

case class OrderProfit(id: String, typ: Int, profit: Int)
class OrderProfitDecoder(props: VerifiableProperties = null) extends Decoder[OrderProfit] {
  import io.circe.parser._

  def fromBytes(bytes: Array[Byte]): OrderProfit = {
    try {
      println("begin fromBytes")
      println(bytes.map(_.toChar).mkString)
      println("end fromBytes")
      decode[OrderProfit](bytes.map(_.toChar).mkString) match {
        case Left(error) => {
          println(error)
          OrderProfit("error", 1, 0)
        }
        case Right(orderProfit) => {
          println("decode")
          println(orderProfit)
          orderProfit
        }
      }
    } catch {
      case e: Exception => println(e); OrderProfit("error2", 1, 0)
    }

  }
}

object OrderProfit {
  import io.circe.generic.semiauto._

  implicit val encodeOrderProfit: IEncoder[OrderProfit] = deriveEncoder[OrderProfit]
  implicit val decodeOrderProfit: IDecoder[OrderProfit] = deriveDecoder[OrderProfit]
}
