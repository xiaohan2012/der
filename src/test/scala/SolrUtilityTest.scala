import org.scalactic.TolerantNumerics
import org.scalatest._
import org.scalatest.Assertions._

import org.hxiao.der.wikipedia.classes._
import org.apache.solr.core.CoreContainer
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.common.params.ModifiableSolrParams
import org.apache.solr.common.params.CommonParams

import org.hxiao.der.util.SolrUtility


class SolrUtilitySpec extends FlatSpec with BeforeAndAfter with Matchers {
  private var server: EmbeddedSolrServer = _

  val epsilon = 1e-4f
  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(epsilon)

  before {
    val solr_dir = getClass().getResource("solr").getPath()
    val container = new CoreContainer(solr_dir);
    container.load();
    server = new EmbeddedSolrServer(container, "test");
  }

  "SolorUtility.addSurfaceNames" should "index a list of surface names in Solr" in {
    val solr_util = new SolrUtility(server)
    val surface_names = List(
      new SurfaceName("two", List((2, 2), (-1, 1))),
      new SurfaceName("2", List((2, 1))),
      new SurfaceName("II", List((2, 1))),
      new SurfaceName("one", List((1, 1))),
      new SurfaceName("Three", List((3, 1)))
    )

    solr_util.addSurfaceNames(surface_names, 2)

    val params = new ModifiableSolrParams();
    params.add(CommonParams.Q, "*:*");
    val res = server.query(params);
    val results = res.getResults

    5 should equal {
      results.getNumFound
    }
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

  after {
    if (server != null) {
      // delete the collection
      server.deleteByQuery( "*:*" )
      server.shutdown()
    }
  }
}
