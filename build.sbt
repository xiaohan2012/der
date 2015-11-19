lazy val commonSettings = Seq(
  organization := "org.hxiao",
  version := "0.1.0",
  scalaVersion := "2.11.5"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "der"  // := is a method on name, some syntactic sugar    
  )

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2")

