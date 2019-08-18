import Shades._

val jlineVersion = "2.14.6"
val jlineDep = "jline" % "jline" % jlineVersion

ThisBuild / version := jlineVersion + "-SNAPSHOT"
ThisBuild / organization := "org.scala-sbt.jline"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / organizationName := "sbt"
ThisBuild / organizationHomepage := Some(url("https://scala-sbt.org/"))
ThisBuild / homepage := Some(url("https://github.com/sbt/shaded-jline"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/sbt/shaded-jline"), "git@github.com:sbt/shaded-jline.git"))
ThisBuild / developers := List(
  Developer("eed3si9n", "Eugene Yokota", "@eed3si9n", url("https://github.com/eed3si9n"))
)
ThisBuild / description := "shaded jline 2"
ThisBuild / licenses := Seq("The BSD License" -> new URL("http://www.opensource.org/licenses/bsd-license.php"))

lazy val root = (project in file("."))
  .aggregate(shadedJline)
  .settings(
    name := "jline root",
    publish / skip := true,
  )

lazy val shadedJline = (project in file("shaded-jline"))
  .configs(Shade)
  .settings(jlineShadeSettings)
  .settings(
    libraryDependencies ++= Seq(jlineDep % Shade),
    name := "shaded-jline",
    autoScalaLibrary := false,
    crossPaths := false,
    exportJars := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
  )
