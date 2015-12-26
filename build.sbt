lazy val commonSettings = Seq(
  organization := "org.hxiao.der",
  version := "0.1.0",
  scalaVersion := "2.10.5"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "der"  // := is a method on name, some syntactic sugar    
  )

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.2.4" % "test",
  "org.apache.spark" %% "spark-core" % "1.5.2",
  "org.apache.solr" % "solr-solrj" % "5.2.0",
  "org.apache.solr" % "solr-core" % "5.2.0",
  "org.opensextant" % "solr-text-tagger" % "2.2",
  "play" % "play_2.10" % "2.1.0"
)


// ensure only one spark context created at each time
parallelExecution in Test := false


assemblyExcludedJars in assembly := { 
  val cp = (fullClasspath in assembly).value
  cp filter {
    path => {
      path.data.getName == "lucene-codecs-5.0.0.jar"
    }
  }
}

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>  {
  case PathList("META-INF", "services", pkg, xs @ _*) if pkg.startsWith("org.apache.lucene")=> MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
}


