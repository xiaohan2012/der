#! /bin/bash

spark-submit --class org.hxiao.der.WikipediaInLinks2JsonDump --master spark://ukko008:50511 target/scala-2.10/der-assembly-0.1.0.jar /cs/home/hxiao/wikipedia/output.xml  /cs/home/hxiao/wikipedia/in-links.json 1000
