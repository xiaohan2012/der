#! /bin/bash

spark-submit --class org.hxiao.der.Wikipedia2SolrProcessor --master local[2] target/scala-2.10/der-assembly-0.1.0.jar /home/hxiao/solr/ test /home/hxiao/code/der/src/test/resources/output-head-100.xml 5
