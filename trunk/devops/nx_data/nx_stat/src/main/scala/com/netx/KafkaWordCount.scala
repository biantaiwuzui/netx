package com.netx

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

object KafkaWordCount extends App {
  val zkQuorum = "127.0.0.1:2181"
  val group = "test"
  val topics = "kafka_spark"
  val numThreads = 2
  val sparkConf = new SparkConf().setAppName("KafkaWordCount")
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  ssc.checkpoint("/checkpoint")

  val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
  val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
  var words = lines.flatMap(_.split(","))
  println(words)
  words.foreachRDD(rdd => {
    println("test:" + rdd.name)
  })

  ssc.start()
  ssc.awaitTermination()
  ssc.stop()
}

