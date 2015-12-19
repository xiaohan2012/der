#! /bin/bash
./scripts/clean-solr-data.sh

spark-submit --class org.hxiao.der.Wikipedia2SolrProcessor --master spark://ukko053:50511 target/scala-2.10/der-assembly-0.1.0.jar /cs/home/hxiao/solr/ surface_names /cs/home/hxiao/wikipedia/output.xml 1000