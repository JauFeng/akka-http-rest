import sbt.Keys._

import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

name := """akka-http-rest"""
description := """ App's REST backend which using akka-http. """
//homepage := Some(new URL(""))

val akkaVersion = "2.4.3"

val reactiveMongoVersion = "0.11.11"

val scalaTestVersion = "2.2.6"
val scalaMockVersion = "3.2.2"
val scalaLoggingVersion = "3.1.0"
val logbackVersion = "1.1.7"

val commonDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-agent" % akkaVersion,
  "com.typesafe.akka" %% "akka-camel" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,

  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-osgi" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-tck" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-distributed-data-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-typed-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-jackson-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-typed-experimental" % akkaVersion,

  // ScalaTest
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % "test",

  // Logback
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-access" % logbackVersion,

  "com.google.inject" % "guice" % "4.0",
  "com.google.guava" % "guava" % "19.0"
)

val mongoDBDependencies = Seq(
  "org.reactivemongo" %% "reactivemongo" % reactiveMongoVersion,
  "org.reactivemongo" %% "reactivemongo-play-json" % reactiveMongoVersion
)

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

lazy val commonSettings = scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(CompactControlReadability, true)
    .setPreference(SpacesAroundMultiImports, false),

  organization := "com.turing",
  organizationName := "Turing Studio",
  organizationHomepage := Some(new URL("https://github.com/JauFeng")),
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.8",

  libraryDependencies ++= (commonDependencies ++ mongoDBDependencies),

  scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")
)


lazy val root = (project in file(".")).settings(commonSettings: _*).aggregate(api, backend, web)

lazy val api = (project in file("api"))
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(RevolverPlugin) // re-start:~
  .enablePlugins(JavaServerAppPackaging) // create a start script in `/etc/init.d` or `/etc/init`
  .settings(commonSettings: _*)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, buildInfoBuildNumber),
    buildInfoPackage := "com.turing.rest.version",
    buildInfoObject := "BuildInfo",
    buildInfoOptions += BuildInfoOption.BuildTime,

    //    mainClass in(Compile, run) := Some("com.turing.rest.Main")
    // Use `assembly` which will compile project, run tests, and then pack class files
    // and dependencies into a single JAR file: `target/scala-2.11/akka-http-rest-assembly-1.0.0-SNAPSHOT.jar`
    mainClass in assembly := Some("com.turing.rest.Main"),
    assemblyJarName in assembly := "akka-http-rest.jar",
    test in assembly := {}
  )
lazy val backend = (project in file("backend")).settings(commonSettings: _*)

lazy val web = (project in file("web")).settings(commonSettings: _*)

fork in run := true