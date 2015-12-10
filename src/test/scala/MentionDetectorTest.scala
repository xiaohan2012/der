import org.hxiao.der.mention.{DictionaryMentionDetector, SolrMapDictionary}
import org.hxiao.der.util.SolrUtility
import org.hxiao.der.wikipedia.classes.{SurfaceName => SF}

import org.scalatest._
import org.scalatest.Assertions._

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory

class MentionDetectorSpec extends FlatSpec with BeforeAndAfter with Matchers {
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
  }

  "MentionDetector.detect" should "return a list of chunks" in {

    val dict = new SolrMapDictionary(solr_server, 100)
    println("dict:", dict)
    val detector = new DictionaryMentionDetector(dict)
    val text = """Down below, bomb-sniffing dogs will patrol the trains and buses that are expected to take approximately 30,000 of the 80,000-plus spectators to Sunday's Super Bowl between the Denver Broncos and Seattle Seahawks."""
    val chunks = detector.detect(text)
    List("Super Bowl", "Denver Broncos", "Seattle Seahawks").sorted should equal {
      detector.mkString(text, chunks).sorted
    }
  }

  after {
    if(solr_server != null){
      solr_server.deleteByQuery( "*:*" )
      solr_server.shutdown()
    }
  }
}
