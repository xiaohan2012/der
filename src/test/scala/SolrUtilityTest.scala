import org.scalactic.TolerantNumerics
import org.scalatest._
import org.scalatest.Assertions._

import org.apache.spark._

import org.apache.solr.core.CoreContainer
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.common.params.ModifiableSolrParams
import org.apache.solr.common.params.CommonParams
import org.apache.solr.common.SolrDocumentList

import org.hxiao.der.wikipedia.classes._
import org.hxiao.der.util.SolrUtility


class SolrUtilitySpec extends FlatSpec with BeforeAndAfter with Matchers {
  private var solr_server: EmbeddedSolrServer = _

  private val master = "local[2]"
  private val appName = "SolrUtilitySpec"
  private var sc: SparkContext = _

  val epsilon = 1e-4f
  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(epsilon)


  before {
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)

    sc = new SparkContext(conf)

    val solr_dir = getClass().getResource("solr").getPath()
    val solr_core_name = "test"
    solr_server = SolrUtility.createEmbeddedSolrServer(solr_dir, solr_core_name)

    val surface_names = List(
      new SurfaceName("two", List((2, 2), (-1, 1))),
      new SurfaceName("2", List((2, 1))),
      new SurfaceName("II", List((2, 1))),
      new SurfaceName("one", List((1, 1))),
      new SurfaceName("Three", List((3, 1)))
    )
    val solr_util = new SolrUtility(solr_server)
    solr_util.addSurfaceNamesFromRDD(sc.parallelize(surface_names))
  }

  def check(results: SolrDocumentList) = {
    val first_doc = results.get(0)
    "two" should equal {
       first_doc.get("surface_name")
    }
    3 should equal {
       first_doc.get("occurrences")
    }
    0.47712125471966244 should === {
       first_doc.get("log_occurrences")
    }
  }


  "SolorUtility.addSurfaceNames" should "index 5 surface names in Solr" in {
    val params = new ModifiableSolrParams();
    params.add(CommonParams.Q, "*:*");
    val res = solr_server.query(params);
    val results = res.getResults
    5 should equal {
      results.getNumFound
    }
    check(results)
  }

  "Querying 'two'" should "return 1 surface name" in {
    val params = new ModifiableSolrParams();
    params.add(CommonParams.Q, "surface_name:two");
    val res = solr_server.query(params);
    val results = res.getResults

    1 should equal {
      results.getNumFound
    }
    check(results)
  }

  after {
    if (solr_server != null) {
      // delete the collection
      solr_server.deleteByQuery( "*:*" )
      solr_server.shutdown()
    }
    if (sc != null) {
      sc.stop()
    }
  }
}
