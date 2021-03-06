import Dependencies.kaleidoDeps

lazy val mainScalaVer = "2.13.6"
lazy val supportedScalaVer = List(mainScalaVer, "2.12.14")

ThisBuild / organization := "org.plotly-scala"
ThisBuild / homepage := Some(url("https://github.com/johanneshiry/kaleido-scala"))
ThisBuild / licenses := Seq("BSD 3-Clause License" ->
  url("https://github.com/johanneshiry/kaleido-scala/blob/main/LICENSE"))
ThisBuild / developers := List(Developer(
  "johanneshiry",
  "Johannes Hiry",
  "info@johannes-hiry.de",
  url("https://github.com/johanneshiry")
))
ThisBuild / scalaVersion := mainScalaVer

lazy val kaleido = (project in file("core"))
  .settings(
    name := "kaleido",
    libraryDependencies ++= kaleidoDeps,
    crossScalaVersions := supportedScalaVer
  )

// root project
crossScalaVersions := Nil
skip / publish := true