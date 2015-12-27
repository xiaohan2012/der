#! /bin/bash

spark-submit --class org.hxiao.der.WikipediaInLinks2JsonDump --master local[2] target/scala-2.10/der-assembly-0.1.0.jar /home/hxiao/code/der/src/test/resources/output-head-100.xml /home/hxiao/code/tmp.json 5
