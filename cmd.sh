#! /bin/bash

spark-submit --class org.hxiao.der.Wikipedia2SolrProcessor --master local[2]  --files src/test/resources/log4j.properties target/scala-2.10/der-assembly-0.1.0.jar src/test/resources/solr/ test src/test/resources/output-head-100.xml
