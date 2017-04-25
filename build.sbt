scalaVersion := "2.12.2"

val http4sVersion = "0.15.9"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.2",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.typelevel" %% "scalaz-scalatest" % "0.5.2" % "test",
  "org.http4s" %% "http4s-circe" % http4sVersion,
  // Optional for auto-derivation of JSON codecs
  "io.circe" %% "circe-generic" % "0.7.1",
  // Optional for string interpolation to JSON model
  "io.circe" %% "circe-literal" % "0.7.1"
)

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfuture",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused"
)
