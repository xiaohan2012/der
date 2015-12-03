package org.hxiao.der

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.hxiao.der.wikipedia.WikipediaProcessor
import org.hxiao.der.util.SolrUtility


object Wikipedia2SolrProcessor {
  def run(solr_dir: String, core_name: String, xml_path: String) = {
    // spark context
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("Wikipedia2SolrProcessor")

    val sc = new SparkContext(conf)

    // solr server
    val server = SolrUtility.createEmbeddedSolrServer(solr_dir, core_name)


    val (title2id, links, surface_names) = WikipediaProcessor.apply(sc, xml_path)

    val solr_util = new SolrUtility(server)
    solr_util.addSurfaceNamesFromRDD(surface_names)


    server.shutdown()
    sc.stop()
  }
  def main(args: Array[String]) = {
    
  }
}
