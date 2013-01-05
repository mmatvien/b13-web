import sbt._
import PlayProject._
import Keys._

object ApplicationBuild extends Build {

  val appName = "b13-web"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "se.radley" %% "play-plugins-salat" % "1.1",
    "commons-codec" % "commons-codec" % "1.5",
    "com.typesafe.akka" % "akka-zeromq" % "2.0.3",
    "com.codahale" % "jerkson_2.9.1" % "0.5.0",
    "commons-codec" % "commons-codec" % "1.4",
    "org.apache.httpcomponents" % "httpclient" % "4.1.1",
    "org.apache.httpcomponents" % "httpcore" % "4.1",
    "commons-logging" % "commons-logging" % "1.1.1",
    "org.apache.httpcomponents" % "httpclient-cache" % "4.1.1",
    "org.apache.httpcomponents" % "httpmime" % "4.1.1"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // scalaVersion := "2.9.2"
    // Add your own project settings here

    resolvers ++= Seq(
      "repo.novus snaps" at "http://repo.novus.com/snapshots/",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "repo.codahale.com" at "http://repo.codahale.com"),

    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport += "org.bson.types.ObjectId"
  )

}
