package org.hxiao.der

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.hxiao.der.wikipedia.WikipediaProcessor


/*
 Collect in-links and save the result to lines of json
 */
object WikipediaInLinks2JsonDump {
  def run(sc: SparkContext, xml_path: String, output_path: String, min_partitions: Int) = {
    val out_links = WikipediaProcessor.extractOutLinks(sc, xml_path, min_partitions)
    val in_links = WikipediaProcessor.extractInLinks(sc, out_links, min_partitions)
    val json_strings = WikipediaProcessor.jsonizeLinks(in_links)
    json_strings.saveAsTextFile(output_path)
  }

  def main(args: Array[String]) = {
    val conf = new SparkConf().setAppName("WikipediaInLinks2JsonDump")
    val sc = new SparkContext(conf)
    val xml_path = args(0)
    val output_path = args(1)
    val min_partitions = args(2).toInt
    println(s"xml_path: ${xml_path}")
    println(s"output_path: ${output_path}")
    run(sc, xml_path, output_path, min_partitions)
  }
}
