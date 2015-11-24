package org.hxiao.der.wikipedia

import org.hxiao.der.wikipedia.classes._
import scala.xml.Elem

object Extractor {

  def extractEntityInfo(page: Elem) = {
    Entity(
      (page \ "id").head.text.toInt,
      (page \ "title").head.text
    )
  }

  def extractPageInfo(page: Elem): RawPageInfo = {
    val anchors = extractAnchors(page)
    val entity = extractEntityInfo(page)
    val links = packLinks(entity, anchors)
    RawPageInfo(entity, anchors, links)
  }

  def packLinks(entity: Entity, anchors: List[RawAnchor]): List[RawLink] = {
    anchors.map { anchor =>
      RawLink(entity.id, anchor.title)
    }
  }

  def extractAnchors(page: Elem) : List[RawAnchor] = {
    // Extract anchors given a XML page
    val text = (page \\ "text").head.text

    val link = """\[\[([^\[\]]+?)(?:\|([^\[\]]+))?\]\]""".r

    (link findAllIn text).map( _ match {
      case link(title, null) => RawAnchor(title, title)
      case link(surface, title) => RawAnchor(surface, title)
    }).toList
  }
}
