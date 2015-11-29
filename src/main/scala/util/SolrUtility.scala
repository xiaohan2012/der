package org.hxiao.der.util

import scala.math.log10
import scala.collection.JavaConversions

import org.apache.solr.common.SolrInputDocument
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer


import org.hxiao.der.wikipedia.classes.SurfaceName


class SolrUtility(server: EmbeddedSolrServer) {
  def addSurfaceNames(surface_names: List[SurfaceName], chunk_size: Int = 100) = {
    // add a batch of chunk_size documents each time
    surface_names.grouped(chunk_size).foreach  {
      sf_list => {
        val docs = sf_list.map {
          sf => {
            val doc = new SolrInputDocument()
            doc.addField("surface_name", sf.name)
            doc.addField("occurrences", sf.occurrences)
            doc.addField("log_occurrences", log10(sf.occurrences))
            doc
          }
        }
        server.add(JavaConversions.asJavaCollection(docs))
      }
    }
    server.commit
  }
}
