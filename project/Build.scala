import sbt._
import play.Project._
import Keys._

object Build extends Build {

  val appName = "b13-web"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "se.radley" %% "play-plugins-salat" % "1.2",
    "commons-codec" % "commons-codec" % "1.5",
    "com.typesafe.akka" % "akka-zeromq" % "2.0.3",
    "com.codahale" % "jerkson_2.9.1" % "0.5.0",
    "commons-codec" % "commons-codec" % "1.4",
    "org.apache.httpcomponents" % "httpclient" % "4.1.1",
    "org.apache.httpcomponents" % "httpcore" % "4.1",
    "commons-logging" % "commons-logging" % "1.1.1",
    "org.apache.httpcomponents" % "httpclient-cache" % "4.1.1",
    "org.apache.httpcomponents" % "httpmime" % "4.1.1",
    "net.liftweb" %% "lift-json" % "2.5-M4",
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(

    resolvers ++= Seq(
      "repo.novus snaps" at "http://repo.novus.com/snapshots/",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "repo.codahale.com" at "http://repo.codahale.com"),

    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport += "org.bson.types.ObjectId"
  )

}
