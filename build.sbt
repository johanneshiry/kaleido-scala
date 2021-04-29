import sbt.Keys._

lazy val scala213 = "2.13.1"
lazy val scala212 = "2.12.13"
lazy val supportedScalaVersions = List(scala213, scala212)

val circeVersion = "0.13.0"
val slf4jVersion = "1.7.30"
val log4jVersion = "2.14.1"

ThisBuild / organization := "com.example"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := scala213

lazy val root = (project in file("."))
  .settings(
    name := "kaleido-scala",
    crossScalaVersions := supportedScalaVersions,
    publish / skip := true,

    // plotly scala
    libraryDependencies += "org.plotly-scala" %% "plotly-render" % "0.8.2",

    // json parsing
    libraryDependencies += "io.circe" %% "circe-core" % circeVersion,
    libraryDependencies += "io.circe" %% "circe-generic" % circeVersion,
    libraryDependencies += "io.circe" %% "circe-parser" % circeVersion,

    // logging
    libraryDependencies += "org.slf4j" % "slf4j-api" % slf4jVersion % "compile", // slf4j wrapper
    libraryDependencies += "com.lmax" % "disruptor" % "3.4.3" % "compile", // async logging
    libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % log4jVersion % "compile", // log4j
    libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % log4jVersion % "compile", // log4j
    libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion % "compile", // log4j -> slf4j

    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3" % "compile", // akka scala logging
    libraryDependencies += "org.slf4j" % "log4j-over-slf4j" % slf4jVersion % "compile", // slf4j -> log4j
  )
