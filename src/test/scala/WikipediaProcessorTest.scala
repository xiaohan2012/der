import org.apache.spark._
import org.scalatest._
import org.scalatest.Assertions._
import scala.collection.immutable.HashMap

import org.hxiao.der.wikipedia.WikipediaProcessor
import org.hxiao.der.wikipedia.classes._

class WikipediaProcessorSpec extends FlatSpec with BeforeAndAfter with Matchers {

  private val master = "local[2]"
  private val appName = "example-spark"

  private var sc: SparkContext = _

  before {
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)

    sc = new SparkContext(conf)
  }

  "WikipediaProcessor.apply" should "return: - a list of links between articles - surface to entity frequency - entity to id mapping" in {
    val xml_path = getClass().getResource("1234-example.xml").getPath()
    val pageInfo = WikipediaProcessor.collectPageInfo(sc, xml_path)
    3 should equal {
      pageInfo.collect().length
    }

    val raw_links = WikipediaProcessor.collectLinks(pageInfo)
    val expected_raw_links = Array(RawLink(1, "Four"), RawLink(1, "Two"), RawLink(2, "Three"), RawLink(3, "Two"), RawLink(3, "One")).sorted
    expected_raw_links should equal (
      raw_links.collect().sorted
    )

    val raw_anchors = WikipediaProcessor.collectAnchors(pageInfo)
    val expected_raw_anchors = Array(
      RawAnchor("two", "Two"), RawAnchor("2", "Two"), RawAnchor("two", "Four"),
      RawAnchor("II", "Two"), RawAnchor("one", "One"), RawAnchor("Three", "Three")
    ).sorted

    expected_raw_anchors should equal (
      raw_anchors.collect().sorted
    )

    val title2id = WikipediaProcessor.collectTitle2Id(pageInfo)
    HashMap("One" -> 1, "Two" -> 2, "Three" -> 3) should equal {
      title2id.collectAsMap()
    }

    // TODO:
    // - redirect normalization
    val links = WikipediaProcessor.normalizeLinks(raw_links, title2id.collectAsMap())
    val expected_links = Array(
      Link(1, WikipediaProcessor.NON_EXIST_ENTITY_ID), Link(1, 2), Link(2, 3), Link(3, 2), Link(3, 1)
    ).sorted
    expected_links should equal {
      links.collect().sorted
    }

    val anchors = WikipediaProcessor.normalizeAnchors(raw_anchors, title2id.collectAsMap())
    val expected_anchors = Array(
      Anchor("two", 2), Anchor("2", 2), Anchor("two", WikipediaProcessor.NON_EXIST_ENTITY_ID),
      Anchor("II", 2), Anchor("one", 1), Anchor("Three", 3)
    ).sorted
    expected_anchors should equal {
      anchors.collect().sorted
    }

    val surface2entity_frequency = WikipediaProcessor.surface2entityFrequency(anchors)
    List(
      ("two", List((2, 1), (WikipediaProcessor.NON_EXIST_ENTITY_ID, 1))),
      ("2", List((2, 1))),
      ("II", List((2, 1))),
      ("one", List((1, 1))),
      ("Three", List((3, 1)))
    ).sortBy {
      case (s, lst) => s
    }.map {
      case (s, lst) => {
        (s, lst.sorted)
      }
    } should equal {
      surface2entity_frequency.collect.toList.sortBy {
        case (s, lst) => s
      }.map {
        case (s, lst) => {
          (s, lst.toList.sorted)
        }
      }
    }
  }

  after {
    if (sc != null) {
      sc.stop()
    }
  }
}
