name := "restaurantsapi"

version := "0.1"
val CatsVersion = "1.6.0"
val CirceVersion = "0.13.0"
scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "com.typesafe" % "config" % "1.3.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "io.monix" %% "monix-reactive" % "3.2.1",
    "io.monix" %% "monix-nio" % "0.0.8",
    "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "2.0.0",
    "com.typesafe.akka" %% "akka-stream" % "2.6.5",
    "com.typesafe.akka" %% "akka-http" % "10.1.12",
    "org.typelevel" %% "cats-core" % "2.1.1",
    "io.circe" %% "circe-generic" % CirceVersion,
    "io.circe" %% "circe-literal" % CirceVersion,
    "io.circe" %% "circe-generic-extras" % CirceVersion,
    "io.circe" %% "circe-parser" % CirceVersion,
    "de.heikoseeberger" %% "akka-http-circe" % "1.32.0",
    "com.softwaremill.macwire" %% "macros"  % "2.3.2"
)

scalacOptions ++= Seq("-language:postfixOps")
