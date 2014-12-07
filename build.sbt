scalaVersion := "2.11.4"

organization := "nu.rinu"

name := "hello-scala"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.scalikejdbc" %% "scalikejdbc"       % "2.2.0",
  //
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalikejdbc" %% "scalikejdbc-test"   % "2.2.0"   % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.0" % "test",
  "mysql" % "mysql-connector-java" % "5.1.34"
)

scalikejdbcSettings
