import org.scalatest._
import org.scalatest.Assertions._
import org.scalactic.TolerantNumerics

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.common.params.ModifiableSolrParams
import org.apache.solr.common.params.CommonParams

import org.hxiao.der.Wikipedia2SolrProcessor
import org.hxiao.der.util.SolrUtility

class Wikipedia2SolrProcessorSpec extends FlatSpec with BeforeAndAfter with Matchers {
  var server: EmbeddedSolrServer = _
  val solr_dir = getClass().getResource("solr").getPath()
  val solr_core_name = "test"
  val xml_path = getClass().getResource("output-head-100.xml").getPath()

  val epsilon = 1e-4f
  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(epsilon)
  

  before {
    Wikipedia2SolrProcessor.run(solr_dir, solr_core_name, xml_path)

    server = SolrUtility.createEmbeddedSolrServer(solr_dir, solr_core_name)
  }

  "Wikipedia2SolrProcessor.run" should "process given XML file and save the anchors into Solr" in {

    7638 should equal {
      val params = new ModifiableSolrParams();
      params.add(CommonParams.Q, s"*:*");
      val res = server.query(params);
      val results = res.getResults
      results.getNumFound
    }

    val target_surface_name = "stateless society"
    val params = new ModifiableSolrParams();
    params.add(CommonParams.Q, s"""surface_name:"$target_surface_name"""");
    val res = server.query(params);
    val results = res.getResults
    1 should equal {
      results.getNumFound
    }

    val first_doc = results.get(0)
    target_surface_name should equal {
       first_doc.get("surface_name")
    }
    1 should equal {
       first_doc.get("occurrences")
    }
    0 should === {
      first_doc.get("log_occurrences")
    }
  }

  after {
    if(server != null){
      server.deleteByQuery( "*:*" )
      server.shutdown()
    }
  }
}
