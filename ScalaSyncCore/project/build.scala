import sbt._
import Keys._

object ScalasyncserverBuild extends Build {
  val Organization = "org.scalasync"
  val Name = "ScalaSyncCore"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.4"
  
  lazy val project = Project (
    "ScalaSyncCore",
    file("."),
    settings = Defaults.defaultSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Casbah " at "https://oss.sonatype.org/content/repositories/releases/",

      libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
      "org.specs2" %% "specs2" % "1.1.3",
      "org.scalatest" %% "scalatest" % "2.0.M5b",
      "com.github.tototoshi" %% "play-json4s-native" % "0.1.0",
      "com.github.tototoshi" %% "play-json4s-test-native" % "0.1.0" % "test",
      "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2",
      "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2",
      "com.typesafe.akka" % "akka-actor_2.10" % "2.3.2",
      "com.typesafe.akka" % "akka-remote_2.10" % "2.3.2",
      "org.mongodb" %% "casbah" % "2.5.0"

/*        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))*/
      )
    )
  )
}
