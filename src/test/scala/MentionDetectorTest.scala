import sys.process._
import java.io.File

import org.hxiao.der.util.SolrUtility
import org.hxiao.der.wikipedia.classes.{SurfaceName => SF}
import org.hxiao.der.mention.{MentionDetector, Mention}

import org.scalatest._
import org.scalatest.Assertions._

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer


class MentionDetectorSpec extends FlatSpec with BeforeAndAfter with Matchers {
  val port = "10987"
  val service_url = s"http://localhost:$port/solr/test/tag?overlaps=NO_SUB&tagsLimit=5000&fl=*&matchText=true"

  val home_dir = System.getProperty("user.home")
  val solr_home = System.getenv("SOLRHOME")
  require(solr_home != null, "env var $SOLRHOME is not set")

  val solr_core_dir = (new File(
    getClass().getResource("output-head-100.xml").getPath())
  ).getParent() + "/solr"
  val start_solr_cmd = s"$solr_home/bin/solr start -s $solr_core_dir -p $port"
  val stop_solr_cmd = s"$solr_home/bin/solr stop  -p $port"

  val solr_dir = getClass().getResource("solr").getPath()
  val solr_core_name = "test"
  var solr_server: EmbeddedSolrServer = _

  before {
    // create some fixtures
    solr_server = SolrUtility.createEmbeddedSolrServer(solr_dir, solr_core_name)

    val solr_util = new SolrUtility(solr_server)
    solr_util.addSurfaceNames(
      List("Super Bowl", "Denver", "Seattle", "Denver Broncos", "Seattle Seahawks").map(
        name => {
          new SF(name, null, 1)
        }
      ).toIterator
    )
    solr_server.shutdown()

    require((start_solr_cmd !) == 0, "solr server starts by cmd FAIL!")
  }

  "MentionDetector.detect" should "return a list of mentions" in {
    val detector = new MentionDetector(service_url)
    val text = """Down below, bomb-sniffing dogs will patrol the trains and buses that are expected to take approximately 30,000 of the 80,000-plus spectators to Sunday's Super Bowl between the Denver Broncos and Seattle Seahawks."""
    val mentions = detector.detect(text)

    List((153, 163, "Super Bowl"),
      (176, 190, "Denver Broncos"),
      (195, 211, "Seattle Seahawks")
    ) should equal {
      mentions.map {
        m => {
          (m.start, m.end, m.text)
        }
      }
    }
  }

  after {
    require((stop_solr_cmd !) == 0, "solr server cmd stop FAIL")

    if(solr_server != null){
      solr_server = SolrUtility.createEmbeddedSolrServer(solr_dir, solr_core_name)
      solr_server.deleteByQuery( "*:*" )
      solr_server.shutdown()
    }
  }
}
