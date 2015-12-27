import java.io.File
import org.apache.commons.io.FileUtils

import org.scalatest._
import org.scalatest.Assertions._
import org.scalactic.TolerantNumerics

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.hxiao.der.WikipediaInLinks2JsonDump

class WikipediaInLinks2JsonDumpSpec extends FlatSpec with BeforeAndAfter with Matchers {
  var sc: SparkContext = _
  var xml_path: String = _
  var output_path: String = _

  before {
    // spark context
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("WikipediaInLinks2JsonDumpSpec")
    sc = new SparkContext(conf)
  }

  "WikipediaInLinks2JsonDumpSpec.run(fake set)" should "save in-link as json into file" in {
    xml_path = getClass().getResource("1234-example.xml").getPath()
    output_path = (new File(xml_path)).getParent() + "/inlinks-fake.json"
    WikipediaInLinks2JsonDump.run(sc, xml_path, output_path, sc.defaultMinPartitions)
    val lines = sc.textFile(output_path)
    4 should equal {
      lines.count
    }
    "[-1,[1]]" should equal {
      lines.collect.sorted.head
    }
  }

  "WikipediaInLinks2JsonDumpSpec.run(real set)" should "save in-link as json into file" in {
    xml_path = getClass().getResource("output-head-100.xml").getPath()
    output_path = (new File(xml_path)).getParent() + "/inlinks-real.json"
    WikipediaInLinks2JsonDump.run(sc, xml_path, output_path, sc.defaultMinPartitions)
    val lines = sc.textFile(output_path)
    10 should equal {
      lines.count
    }

    lines.collect.sorted.head should startWith ("[-1,[")
  }

  after {
    if(sc != null){
      sc.stop()
    }

    val d = new File(output_path)
    if(d.isDirectory())
      FileUtils.deleteDirectory(d)
  }
}
