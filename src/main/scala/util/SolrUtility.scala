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

  def addSurfaceName(sf: SurfaceName): Unit = {
    addSurfaceName(sf.name, sf.occurrences)
  }

  def addSurfaceName(surface: String, occ: Int): Unit = {
    val doc = new SolrInputDocument()
    doc.addField("surface_name", surface)
    doc.addField("occurrences", occ)
    doc.addField("log_occurrences", log10(occ))
    server.add(doc)
  }

  def addSurfaceNamesFromRDD(surface_names: RDD[SurfaceName]) = {
    surface_names.toLocalIterator.foreach(addSurfaceName)
    server.commit
  }

  def addSurfaceNamesFromRDD(surface_names: => RDD[(String, Int)]) = {
    surface_names.toLocalIterator.foreach{
      case (suf, occ) =>
        addSurfaceName(suf, occ)
    }
    server.commit
  }

  def addSurfaceNames(surface_names: Iterator[SurfaceName]) = {
    surface_names.foreach(addSurfaceName)
    server.commit
  }
}
