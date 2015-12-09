#! /bin/bash

spark-submit --class org.hxiao.der.Wikipedia2SolrProcessor --master spark://ukko017:50511 target/scala-2.10/der-assembly-0.1.0.jar /cs/home/hxiao/solr/ surface_names src/test/resources/output-head-100.xml
