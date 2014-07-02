import sbt._
import Keys._

object ScalasyncserverBuild extends Build {
  val Organization = "org.scalasync"
  val Name = "ScalaSyncClient"
  val Version = "0.1.0"
  val ScalaVersion = "2.10.4"
  
  lazy val project = Project (
    "ScalaSyncClient",
    file("."),
    settings = Defaults.defaultSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Casbah " at "https://oss.sonatype.org/content/repositories/releases/",

      libraryDependencies ++= Seq(
      "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
      "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
      "org.specs2" %% "specs2" % "1.13",
      "org.scalatest" %% "scalatest" % "2.0.M5b",
      "org.scalasync" %% "scalasynccore" % "0.1.0"

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
