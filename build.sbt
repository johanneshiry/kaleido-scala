import Dependencies.kaleidoDeps

lazy val mainScalaVer = "2.13.5"

ThisBuild / organization := "org.plotly-sala"
ThisBuild / homepage := Some(url("https://github.com/johanneshiry/kaleido-scala"))
ThisBuild / licenses := Seq("BSD 3-Clause \"New\" or \"Revised\" License" ->
  url("https://github.com/johanneshiry/kaleido-scala/blob/main/LICENSE"))
ThisBuild / developers := List(Developer(
  "johanneshiry",
  "Johannes Hiry",
  "info@johannes-hiry.de",
  url("https://github.com/johanneshiry")
))
ThisBuild / scalaVersion := mainScalaVer

lazy val kaleido = (projectMatrix in file("core"))
  .settings(
    name := "kaleido",
    libraryDependencies ++= kaleidoDeps
  )
  .jvmPlatform(scalaVersions = Seq(mainScalaVer, "2.12.12", "2.12.13", "2.12.14"))



