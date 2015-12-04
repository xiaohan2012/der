lazy val commonSettings = Seq(
  organization := "org.hxiao.der",
  version := "0.1.0",
  scalaVersion := "2.11.5"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "der"  // := is a method on name, some syntactic sugar    
  )

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
  "org.apache.spark" %% "spark-core" % "1.5.2",
  "org.apache.solr" % "solr-solrj" % "5.3.1",
  "org.apache.solr" % "solr-core" % "5.3.1"
)


// ensure only one spark context created at each time
parallelExecution in Test := false

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
   {
     case PathList("META-INF", xs @ _*) => MergeStrategy.discard
     case x => MergeStrategy.first
   }
}
