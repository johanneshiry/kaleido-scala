import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  /// versions
  val circeVersion = "0.13.0"
  val slf4jVersion = "1.7.30"
  val log4jVersion = "2.14.1"

  /// libs
  // plotly scala
  val plotly = "org.plotly-scala" %% "plotly-render" % "0.8.2"

  // json parsing
  val circeCore = "io.circe" %% "circe-core" % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  val circeParser = "io.circe" %% "circe-parser" % circeVersion

  // logging
  val slf4japi = "org.slf4j" % "slf4j-api" % slf4jVersion // slf4j wrapper
  val lMaxDisruptor = "com.lmax" % "disruptor" % "3.4.3" // async logging
  val log4jApi = "org.apache.logging.log4j" % "log4j-api" % log4jVersion // log4j
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % log4jVersion // log4j
  val log4jSlf4jImpl = "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion // log4j -> slf4j

  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3" // akka scala logging
  val log4jOverSlf4j = "org.slf4j" % "log4j-over-slf4j" % slf4jVersion // slf4j -> log4j

  // projects
  val kaleidoDeps =
    Seq(plotly, circeCore, circeGeneric, circeParser, slf4japi, lMaxDisruptor,
      log4jApi, log4jCore, log4jSlf4jImpl, scalaLogging, log4jOverSlf4j)

}
