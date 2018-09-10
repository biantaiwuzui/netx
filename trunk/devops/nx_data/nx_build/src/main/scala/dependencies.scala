import sbt._
import sbt.Keys._

object Dependencies {

  lazy val http = {
    val akkaHttpVersion = "10.1.1"
    Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
      "btomala" %% "akka-http-twirl" % "1.2.0",
      "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",
      "com.softwaremill.akka-http-session" %% "core" % "0.5.5"
    )
  }

  lazy val cats = {
    val catsVersion = "1.0.1"
    val circeVersion = "0.9.1"

    Seq(
      "org.typelevel" %% "cats-core",
      "org.typelevel" %% "cats-free"
    ).map(_ % catsVersion) ++
      Seq(
        "io.circe" %% "circe-core",
        "io.circe" %% "circe-generic",
        "io.circe" %% "circe-parser",
        "io.circe" %% "circe-java8"
      ).map(_ % circeVersion)
  }

  lazy val validation = {
    Seq(
      "org.scalacheck" %% "scalacheck" % "1.13.4",
      "org.scalatest" %% "scalatest" % "3.0.1",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0"
    ).map(_ % Test)
  }

  lazy val akka = {
    val akkaVersion = "2.5.11"
    Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion excludeAll (ExclusionRule("io.netty")),
      "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
      "com.typesafe.akka" %% "akka-distributed-data" % akkaVersion,
      "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % "2.5.14",
      "com.twitter" %% "chill-akka" % "0.9.2",
      "com.google.protobuf" % "protobuf-java" % "2.5.0"
    )
  }

  lazy val persistence = {
    val version = "0.83"
    val quillVersion = "2.3.2"
    Seq(
      "com.typesafe.akka" %% "akka-persistence-cassandra" % version excludeAll (ExclusionRule("io.netty")),
      "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % version % Test,
      "org.iq80.leveldb" % "leveldb" % "0.7",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
      "com.h2database" % "h2" % "1.4.196",
      "org.postgresql" % "postgresql" % "42.1.4",
      "io.getquill" %% "quill-jdbc" % quillVersion,
      "io.getquill" %% "quill-cassandra" % quillVersion,
      "io.getquill" %% "quill-async-postgres" % quillVersion,
      "com.micronautics" %% "quill-cache" % "3.5.9"
//      "io.monix" % "monix_2.12" % "2.3.3"
    )
  }

  lazy val others = {
    Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.github.t3hnar" %% "scala-bcrypt" % "3.1",
      "com.github.jurajburian" %% "mailer" % "1.2.2",
      "commons-codec" % "commons-codec" % "1.11"
    )
  }

  lazy val kafka = {
    val kafkaVersion = "1.1.0"
    val kafkaSerializationV = "0.1.23"
    Seq(
      "org.apache.kafka" %% "kafka" % kafkaVersion exclude("org.slf4j", "slf4j-log4j12"),
      "com.ovoenergy" %% "kafka-serialization-core" % kafkaSerializationV,
      "com.ovoenergy" %% "kafka-serialization-circe" % kafkaSerializationV
    )
  }

  lazy val di = {
    val macwireVersion = "2.3.0"
    Seq(
      "com.softwaremill.macwire" %% "macros" % macwireVersion % "provided",
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % "provided",
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion
    )
  }

  lazy val canal = {
    val canalVersion = "1.1.0"
    Seq(
      "com.alibaba.otter" % "canal.client" % canalVersion,
      "com.alibaba.otter" % "canal.protocol" % canalVersion
    )
  }
  lazy val akkaKafka = {
    Seq("com.typesafe.akka" %% "akka-stream-kafka" % "0.20")
  }

  lazy val shapeless = {
    Seq("com.chuusai" %% "shapeless" % "2.3.3")
  }

  lazy val spark = {
    val sparkVersion = "2.2.0"
    Seq(
      "org.apache.spark" %% "spark-core"                  % sparkVersion % "provided"  excludeAll(ExclusionRule("com.twitter")) ,
      "org.apache.spark" %% "spark-sql" % sparkVersion ,
      "org.apache.spark" %% "spark-streaming"             % sparkVersion % "provided" ,
      "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.2.0",
      "org.apache.kafka" % "kafka-clients" % "0.11.0.1",
      "mysql" % "mysql-connector-java" % "5.1.6"
    )
  }

  lazy val sparkDebug = {
    val sparkVersion = "2.2.0"
    Seq(
      "org.apache.spark" %% "spark-core"                  % sparkVersion  excludeAll(ExclusionRule("com.twitter")) ,
      "org.apache.spark" %% "spark-streaming"             % sparkVersion  ,
      "org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.2.0",
      "org.apache.kafka" % "kafka-clients" % "0.11.0.1"
      //"org.apache.spark" % "spark-streaming-kafka_2.11"       % "1.6.3"
      //        excludeAll(ExclusionRule("org.apache.kafka"),ExclusionRule("org.scala-lang.modules"))
    )
  }

  lazy val redis = Seq("redis.clients" % "jedis" % "2.6.0")
  lazy val backendDeps = akka  ++ cats ++ validation  ++ kafka  ++ canal ++ akkaKafka ++ shapeless ++ redis

  lazy val sparkDeps = akka ++ http ++ cats ++ others  ++ di ++ akkaKafka ++ spark

}
