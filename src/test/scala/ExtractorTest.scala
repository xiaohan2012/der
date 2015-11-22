import org.hxiao.der.wikipedia.{Extractor, Anchor, Link, Entity, PageInfo}

import scala.xml.{XML, Elem}
import org.scalatest.{FlatSpec, BeforeAndAfter}
import org.scalatest.Assertions._

class ExtractorTest extends FlatSpec with BeforeAndAfter {

  var page: Elem = _

  before {
    val path: String = getClass.getResource("page-example.xml").getPath()
    page = XML.loadFile(path)
  }

  "Extractor.extractAnchors" should "extract a list of anchors for Wikipedia page source" in {
    
    val anchors = Extractor.extractAnchors(page)
    assertResult(400){
      anchors.length
    }
    val title = "Brazilian Silicon Valley"
    assertResult(Anchor(title, title)){
      anchors(0)
    }
  }

  "Extractor.extractEntityInfo" should "extract the entity the page is aimed for" in {
    assertResult(Entity(63514, "Campinas")){
      Extractor.extractEntityInfo(page)
    }
  }

  "Extractor.packLinks" should "return the links the page contains" in {
    val anchors = Extractor.extractAnchors(page)
    val entity = Extractor.extractEntityInfo(page)
    val links = Extractor.packLinks(entity, anchors)
    assertResult(400){
      links.length
    }
    assertResult(Link(63514, "Brazilian Silicon Valley")){
      links(0)
    }
  }

  "Extractor.extractPageInfo" should "return page entity, anchors and links in PageInfo object" in  {
    val page_info = Extractor.extractPageInfo(page)
    assertResult(400){
      page_info.anchors.length
    }
    assertResult(400){
      page_info.links.length
    }
    assertResult(Entity(63514, "Campinas")){
      page_info.entity
    }
  }
}
