import Dependencies.kaleidoDeps

lazy val mainScalaVer = "2.13.5"

ThisBuild / organization := "com.example"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := mainScalaVer

lazy val kaleido = (projectMatrix in file("core"))
  .settings(
    name := "kaleido",
    libraryDependencies ++= kaleidoDeps
  )
  .jvmPlatform(scalaVersions = Seq(mainScalaVer, "2.12.12"))



