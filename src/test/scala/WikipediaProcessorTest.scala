
import org.apache.spark._
import org.scalatest._
import org.scalatest.Assertions._
import play.api.libs.json._

import scala.collection.immutable.HashMap

import org.hxiao.der.wikipedia.WikipediaProcessor
import org.hxiao.der.wikipedia.classes._

class WikipediaProcessorSpec extends FlatSpec with BeforeAndAfter with Matchers {

  private val master = "local[2]"
  private val appName = "WikipediaProcessorSpec"

  private var sc: SparkContext = _

  private val xml_path_fake = getClass().getResource("1234-example.xml").getPath()
  private val xml_path_real = getClass().getResource("output-head-100.xml").getPath()

  // some expected values
  val expected_title2id = HashMap("One" -> 1, "Two" -> 2, "Three" -> 3)

  val expected_links = Array(
    Link(1, WikipediaProcessor.NON_EXIST_ENTITY_ID), Link(1, 2), Link(2, 3), Link(3, 2), Link(3, 1)
  ).sorted

  val expected_surface_names = List(
    new SurfaceName("two", List((2, 1), (WikipediaProcessor.NON_EXIST_ENTITY_ID, 1))),
    new SurfaceName("2", List((2, 1))),
    new SurfaceName("II", List((2, 1))),
    new SurfaceName("one", List((1, 1))),
    new SurfaceName("Three", List((3, 1)))
  ).sorted.map(
    s => new SurfaceName(s.name, s.entity_count.sorted, s.occurrences)
  )

  val expected_out_links = List(
      (1, Set(WikipediaProcessor.NON_EXIST_ENTITY_ID, 2)),
      (2, Set(3)),
      (3, Set(1, 2))
    )
  before {
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)

    sc = new SparkContext(conf)
  }

  "A list of methods of WikipediaProcessor" should "return: - a list of links between articles - surface to entity frequency - entity to id mapping" in {

    val pageInfo = WikipediaProcessor.collectPageInfo(sc, xml_path_fake, sc.defaultMinPartitions)

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
    expected_title2id should equal {
      title2id.collectAsMap()
    }

    val links = WikipediaProcessor.normalizeLinks(raw_links, title2id.collectAsMap())
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

    val surface_names = WikipediaProcessor.collectSurfaceNames(anchors, ignoreTable=false)
    
    expected_surface_names should equal {
      surface_names.collect.toList.sorted.map {
        s => new SurfaceName(s.name, s.entity_count.toList.sorted, s.occurrences)
      }
    }

    val surface_names_without_tables = WikipediaProcessor.collectSurfaceNames(anchors, ignoreTable=true)
    
    val expected_surface_names_without_tables = expected_surface_names.map(s=> {new SurfaceName(s.name, null, s.occurrences)}).sorted
    expected_surface_names_without_tables should equal {
      surface_names_without_tables.collect.toList.sorted.map {
        s => new SurfaceName(s.name, null, s.occurrences)
      }
    }

    val surface_names_with_only_count = WikipediaProcessor.collectSurfaceCount(anchors)
    expected_surface_names_without_tables should equal {
      surface_names_with_only_count.collect.toList.sorted
    }

  }

  "WikipediaProcessor.apply(faked set)" should "return: title2id, links, surface2entity frequency as expected" in {
    val (title2id, links, surface_names) = WikipediaProcessor.apply(sc, xml_path_fake, ignoreTable=false)
    expected_title2id should equal {
      title2id
    }

    expected_links should equal {
      links.collect().sorted
    }

    expected_surface_names should equal {
      surface_names.collect.toList.sorted.map(
        s => new SurfaceName(s.name, s.entity_count.toList.sorted, s.occurrences)
      )
    }
  }

  "WikipediaProcessor.apply(real set)" should "return: title2id, links, surface2entity frequency" in {
    val (title2id, links, surface_names) = WikipediaProcessor.apply(sc, xml_path_real)
    12 should equal {
      title2id.getOrElse("Anarchism", -1)
    }
    7638 should equal {
      surface_names.collect.length
    }

    8041 should equal {
      links.collect.length
    }
  }

  "WkikipediaProcessor.extractOutLinks(fake set)" should "return Seq[(Int, Seq[Int])]" in {
    val links = WikipediaProcessor.extractOutLinks(sc, xml_path_fake).collect
    3 should equal {
      links.length
    }

    expected_out_links should equal {
      links.toList
    }
  }
  "WkikipediaProcessor.extractInLinks(fake set)" should "return Seq[(Int, Seq[Int])]" in {
    val expected_in_links = Set(
      (WikipediaProcessor.NON_EXIST_ENTITY_ID, Set(1)),
      (1, Set(3)),
      (2, Set(1, 3)),
      (3, Set(2))
    )
    expected_in_links should equal {
      WikipediaProcessor.extractInLinks(sc,
        sc.parallelize(expected_out_links)
      ).collect.toSet
    }
  }

  "WkikipediaProcessor.extractOutLinks(real set)" should "return Seq[(Int, Seq[Int])]" in {
    val links = WikipediaProcessor.extractOutLinks(sc, xml_path_real).collect
    (100 - 72) should equal {
      links.length
    }
    12 should equal {
      links(0)._1
    }
    1 should equal {
      links(0)._2.size
    }
  }

  "WkikipediaProcessor.jsonizeLinks" should "return RDD[String] json format" in {
    val output = WikipediaProcessor.jsonizeLinks(sc.parallelize(expected_out_links)).collect.toList
    output.zip(expected_out_links) foreach {
      case (string, link) => {
        val actual = Json.parse(string)
        link._1 should equal {
           actual(0).as[Int]
        }
        link._2 should equal {
          actual(1).as[Set[Int]]
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
