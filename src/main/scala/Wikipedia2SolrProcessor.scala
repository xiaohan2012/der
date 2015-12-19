package org.hxiao.der

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.hxiao.der.wikipedia.WikipediaProcessor
import org.hxiao.der.util.SolrUtility


object Wikipedia2SolrProcessor {
  def run(sc: SparkContext, solr_dir: String, core_name: String, xml_path: String, min_partitions: Int) = {
    // solr server
    val server = SolrUtility.createEmbeddedSolrServer(solr_dir, core_name)

    val surface_names = WikipediaProcessor.extractSurfaceCountTuples(sc, xml_path, min_partitions)

    val solr_util = new SolrUtility(server)
    solr_util.addSurfaceNamesFromRDD(surface_names)

    server.shutdown()
  }

  def main(args: Array[String]) = {
    val conf = new SparkConf().setAppName("Wikipedia2SolrProcessor")
    val sc = new SparkContext(conf)
    val solr_dir = args(0)
    val core_name = args(1)
    val xml_path = args(2)
    val min_partitions = args(3).toInt
    println(s"solr_dir: ${solr_dir}")
    println(s"core_name: ${core_name}")
    println(s"xml_path: ${xml_path}")
    run(sc, solr_dir, core_name, xml_path, min_partitions)
  }
}
