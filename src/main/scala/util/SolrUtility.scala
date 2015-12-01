package org.hxiao.der.util

import scala.math.log10
import scala.collection.JavaConversions

import org.apache.spark.rdd.RDD

import org.apache.solr.core.CoreContainer
import org.apache.solr.common.SolrInputDocument
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer

import org.hxiao.der.wikipedia.classes.SurfaceName

object SolrUtility {
  def createEmbeddedSolrServer(solr_dir: String, core_name: String): EmbeddedSolrServer = {
    val container = new CoreContainer(solr_dir);
    container.load();
    new EmbeddedSolrServer(container, core_name)
  }
}

class SolrUtility(server: EmbeddedSolrServer) {
  def addSurfaceNamesFromRDD(surface_names: RDD[SurfaceName]) = {
    surface_names.toLocalIterator.foreach {
      sf => {
        println("****", sf)
        val doc = new SolrInputDocument()
        doc.addField("surface_name", sf.name)
        doc.addField("occurrences", sf.occurrences)
        doc.addField("log_occurrences", log10(sf.occurrences))
        server.add(doc)
      }
    }
    server.commit
  }
}
