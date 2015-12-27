# Wikipedia related

- [Wikipedia dump link](https://dumps.wikimedia.org/enwiki/20151102/enwiki-20151102-pages-articles-multistream.xml.bz2)


# Popular

- [API: Spark 1.5.2 for Scala](http://spark.apache.org/docs/latest/api/scala/index.html)
- [ByKey* functions in Spark](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.PairRDDFunctions)

# Learning resources

- [Good tutorial on Scala XML](https://bcomposes.wordpress.com/2012/05/04/basic-xml-processing-with-scala/): read, query, iterate, convert to/from object
- [Get method names for Scala object](http://stackoverflow.com/questions/2886446/how-to-get-methods-list-in-scala)
- [Scala interpreter print longer traces](http://stackoverflow.com/questions/3767808/how-to-force-interpreter-show-complete-stack-trace/3769827#3769827)
- [Regular expression with lazy matching and non-capturing group](http://stackoverflow.com/questions/8213837/optional-grouping-in-scala-regular-expressions), [regular expression web tool](http://regexr.com/2v8m4) and my work: [regexp on Wiki markup freelink](http://regexr.com/3c87k)
- [Parse huge xml files](http://www.lucasallan.com/2014/12/23/parsing-huge-xml-files-in-scala.html)
- [Emacs as Scala IDE](http://www.troikatech.com/blog/2014/11/26/ensime-and-emacs-as-a-scala-ide)
- [Get test resource path](http://stackoverflow.com/questions/23831768/scala-get-file-path-of-file-in-resources-folder)
- [ScalaTest](http://www.scalatest.org/user_guide/writing_your_first_test)
- [Tail recursion](http://oldfashionedsoftware.com/2008/09/27/tail-recursion-basics-in-scala/): very good example
  - way to tell if tail recursion: if first compute, then recursive call, then it's, otherwise, it's not.
- [foldLeft and foldRight](http://oldfashionedsoftware.com/2009/07/10/scala-code-review-foldleft-and-foldright/) plus some reminder on tail recursion
- [Chop newline character](http://alvinalexander.com/scala/scala-string-chomp-chop-function-newline-characters)
- [BufferedWriter and FileWriter in Java](http://stackoverflow.com/questions/12350248/java-difference-between-filewriter-and-bufferedwriter)
- [Scala run jar](http://stackoverflow.com/questions/2930146/running-scala-apps-with-java-jar): `scala -cp  target/scala-2.11/der_2.11-0.1.0.jar  org.hxiao.der.util.PageXMLFlattener` 
- [Spark unit testing example](http://mkuthan.github.io/blog/2015/03/01/spark-unit-testing/)
- [Tuples to Map](http://stackoverflow.com/questions/6522459/scala-map-from-tuple-iterable)
- [Spark reduceByKey and groupByKey performance issue](https://databricks.gitbooks.io/databricks-spark-knowledge-base/content/best_practices/prefer_reducebykey_over_groupbykey.html)
- [Spark foldByKey](http://blog.madhukaraphatak.com/spark-rdd-fold/)
- [Overloading case class](http://stackoverflow.com/questions/2400794/overload-constructor-for-scalas-case-classes)
- [typedef](http://stackoverflow.com/questions/21223051/typedef-in-scala) and [placing type in package object](http://stackoverflow.com/questions/7441277/scala-type-keyword-how-best-to-use-it-across-multiple-classes)
- [flatMap](http://stackoverflow.com/questions/23138352/how-to-flatten-a-collection-with-spark-scala)
- [sorted and sortWith](http://alvinalexander.com/scala/how-sort-scala-sequences-seq-list-array-buffer-vector-ordering-ordered) and [implicit ordering](http://stackoverflow.com/questions/19345030/easy-idiomatic-way-to-define-ordering-for-a-simple-case-class)
- [ScalaTest should DSL, Matchers](http://www.scalatest.org/user_guide/using_matchers#checkingEqualityWithMatchers)
- [create a class meanwhile modifying its definition](http://stackoverflow.com/questions/3648870/scala-using-hashmap-with-a-default-value)
- [spark groupBy](http://homepage.cs.latrobe.edu.au/zhe/ZhenHeSparkRDDAPIExamples.html#groupBy)
- [mapValues and frequency calculation](http://stackoverflow.com/questions/12105130/generating-a-frequency-map-for-a-string-in-scala)
- [Chunker/NER using Solr](http://sujitpal.blogspot.fi/2013/07/dictionary-backed-named-entity.html)
- Solr
  - [API: add document programically](https://wiki.apache.org/solr/Solrj)
  - [query syntax](https://wiki.apache.org/solr/CommonQueryParameters#fl)
- [Option type](http://danielwestheide.com/blog/2012/12/19/the-neophytes-guide-to-scala-part-5-the-option-type.html)
- [Exception handling and pattern matching](http://danielwestheide.com/blog/2012/12/26/the-neophytes-guide-to-scala-part-6-error-handling-with-try.html)
- [When to use new operator?](https://stackoverflow.com/questions/9727637/new-keyword-in-scala/9727784#9727784)
- [split into chunks(grouped)](http://stackoverflow.com/questions/7459174/split-list-into-multiple-lists-with-fixed-number-of-elements)
- If `XX is not member of XXX` appears over and over though you tried many versions of packages, use `jar tf XX.jar | grep` to check.
- [JavaConversions](http://www.scala-lang.org/api/current/index.html#scala.collection.JavaConversions$)
- [testOnly in sbt](http://stackoverflow.com/questions/11159953/scalatest-in-sbt-is-there-a-way-to-run-a-single-test-without-tags)
- [Core definition(new) in Solr](https://cwiki.apache.org/confluence/display/solr/Defining+core.properties)
- [Solr schema data types](https://cwiki.apache.org/confluence/display/solr/Field+Types+Included+with+Solr)
- [ScalaTest almost equal](http://stackoverflow.com/questions/29938653/scalatest-check-for-almost-equal-for-floats-and-objects-containing-floats/29940436#29940436)
- [Spark partition opeation(quite a lot)](https://spark.apache.org/docs/1.0.0/api/java/org/apache/spark/rdd/RDD.html)
- [RDD.toLocalIterator](https://spark.apache.org/docs/1.0.0/api/java/org/apache/spark/rdd/RDD.html#toLocalIterator%28%29)
- [sequence test execution in SBT(create one spark context at a time)](http://stackoverflow.com/questions/15145987/how-to-run-specifications-sequentially)
- [Solr, OverlappingFileLockException -> core not available](http://stackoverflow.com/questions/5898977/solr-overlappingfilelockexception-when-concurrent-commits)
- [Solr Query Syntax](http://www.solrtutorial.com/solr-query-syntax.html)
- [Submit Spark application](https://spark.apache.org/docs/1.1.0/submitting-applications.html)
- [SBT Assembly: create fat jar](https://github.com/sbt/sbt-assembly) and [Solution for scala 2.11](http://stackoverflow.com/questions/28459333/how-to-build-an-uber-jar-fat-jar-using-sbt-within-intellij-idea)
- [classpath in sbt](http://stackoverflow.com/questions/21698205/how-to-display-classpath-used-for-run-task)
- [Spark test in standalone cluster](http://eugenezhulenev.com/blog/2014/10/18/run-tests-in-standalone-spark-cluster/)
- [Spark for Scala 2.11](http://spark.apache.org/docs/latest/building-spark.html#building-for-scala-211)
- [Unmanaged JARs in sbt](http://www.scala-sbt.org/release/tutorial/Library-Dependencies.html)
- ["Object is not a value" error](http://stackoverflow.com/questions/9079129/object-is-not-a-value-error-in-scala)
- Suppress Solr update log: `log4j.logger.org.apache.solr.core=WARN` and `log4j.logger.org.apache.solr.update.processor=WARN` in `log4j.properties`
- [kill -9](http://unix.stackexchange.com/questions/5642/what-if-kill-9-does-not-work)
- [Column based storage](https://en.wikipedia.org/wiki/Column-oriented_DBMS) and [Parquet](https://parquet.apache.org/)
- [double definition after compiler type eraser](http://stackoverflow.com/questions/3307427/scala-double-definition-2-methods-have-the-same-type-erasure)
- [Talk: Dictionary Based Annotation at Scale with Spark SolrTextTagger and OpenNLP](https://www.youtube.com/watch?v=gOe0aYAS8Do): more on optimization
- [ClassCastException](http://stackoverflow.com/questions/3511169/java-lang-classcastexception) and check the software dependency version
- [Solr add classpath](https://cwiki.apache.org/confluence/display/solr/Lib+Directives+in+SolrConfig)
- [Adding Play Json to sbt](http://stackoverflow.com/questions/19436069/adding-play-json-library-to-sbt)
- [Play Json example](https://www.playframework.com/documentation/2.1.1/ScalaJson)
- [Five ways to create Scala List](http://alvinalexander.com/scala/how-create-scala-list-range-fill-tabulate-constructors)
- [Scala Set](http://www.scala-lang.org/docu/files/collections-api/collections_7.html)
