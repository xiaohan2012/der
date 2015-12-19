
# Lessions learned:

## `sbt assembly` dependency error

I excluded all files under `META-INF` dirs. However, for *SPI classes*, some files cannot be ignored under that dir. Thus, include the corresponding files in `META-INF`

## Performance tips(memory)

Be cautious using `groupBy` in Spark.

Maybe `reduceByKey` or `aggregateByKey` is your choice 
[Scala merge two counters](http://stackoverflow.com/a/7080321/557067)

## Stream closed error

[SO question](http://stackoverflow.com/questions/34299989/spark-ioexception-stream-closed-during-logging/34321585)

Cause: too much parallelism and taking YARN config as standalone mode config
Solution: reduce parallelism and config according to standalone mode 
