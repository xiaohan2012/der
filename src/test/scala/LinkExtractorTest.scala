import org.hxiao.wikipedia.LinkExtractor

import org.scalatest.FlatSpec
import org.scalatest.Assertions._

class LinkExtractorTest extends FlatSpec {

  "A LinkExtractor" should "extract a list of links for Wikipedia page source" in {
    val path: String = getClass.getResource("page-example.xml").getPath()
    val links = LinkExtractor.extract(path).toList
    assertResult(400){
      links.length
    }
    val title = "Brazilian Silicon Valley"
    assertResult((title, title)){
      links(0)
    }
  }
}
