import SbtNetx._

name := "canal-consumer"

libraryDependencies ++= reactiveBackendDeps

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}

assemblyJarName in assembly := "canal-consumer.jar"
mainClass in assembly := Some("com.netx.SyncApp")