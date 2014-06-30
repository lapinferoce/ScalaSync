

/* basic project info */
name := "ScalaSync"

organization := "geek.repo"

version := "0.1.0-SNAPSHOT"

description := "this project can foo a bar!"

scalaVersion := "2.10.2"

startYear := Some(2014)




resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Casbah " at "https://oss.sonatype.org/content/repositories/releases/"

  /* dependencies */
libraryDependencies ++= Seq (
   "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
   "org.specs2" %% "specs2" % "1.13",
   "org.scalatest" % "scalatest_2.10" % "2.0.M5b"
)

libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "play-json4s-native" % "0.1.0",
  "com.github.tototoshi" %% "play-json4s-test-native" % "0.1.0" % "test"
)

libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "play-json4s-jackson" % "0.1.0",
  "com.github.tototoshi" %% "play-json4s-test-jackson" % "0.1.0" % "test"
)

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"



libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.2.3"

libraryDependencies += "com.typesafe.akka" % "akka-remote_2.10" % "2.2.3"

libraryDependencies += "org.mongodb" %% "casbah" % "2.5.0"
