import sbt._
import sbt.Keys._

object SbtNetx extends AutoPlugin {
  override def requires = plugins.JvmPlugin

  override def trigger = allRequirements

  override lazy val projectSettings = commonSettings


  lazy val reactiveBackendDeps = Dependencies.backendDeps

  lazy val sparkBackendDeps = Dependencies.sparkDeps

  lazy val commonSettings = Seq(
    organization := "org.jetmint",
    version := "1.0.0",
    //scalaVersion := "2.12.4",
    scalaVersion := "2.11.8",
    scalacOptions in Compile ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint"),
    javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
    javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m"),
    resolvers ++= Seq(
      "micronautics/scala on bintray" at "http://dl.bintray.com/micronautics/scala",
      "Bartek's repo at Bintray" at "https://dl.bintray.com/btomala/maven",
      Resolver.bintrayRepo("ovotech", "maven"),
      Resolver.bintrayRepo("hseeberger", "maven"),
      "apache-snapshots" at "http://repository.apache.org/snapshots/"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    fork in run := false,
    // disable parallel tests
    parallelExecution in Test := false
  )
}
