
//###################### Parameteres ###################

val _name = "Employe-Transport-Allocation-System"
val _version = "0.1"
val _scalaVersion = "2.13.1"
val akka_http = "10.1.10"
val akka_stream = "2.5.23"
val doobie = "0.8.4"
val mysql = "8.0.18"
val spray_json = "1.3.5"
val pure_config = "0.12.1"

//##################### Settings #######################

val scalaOps = Seq(

)

val commonSettings = Seq(
  version := _version,
  scalaVersion := _scalaVersion,
  scalacOptions ++= scalaOps
)

val dependencies = Seq(
  "com.typesafe.akka" %% "akka-http" % akka_http,
  "com.typesafe.akka" %% "akka-stream" % akka_stream,
  "org.tpolecat" %% "doobie-core" % doobie,
  "mysql" % "mysql-connector-java" % mysql,
  "io.spray" %% "spray-json" % spray_json,
  "com.github.pureconfig" %% "pureconfig" % pure_config
)

lazy val root = (project in file("."))
  .settings(name := _name)
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)