package org.hxiao.der.wikipedia

import scala.xml.Elem

case class Entity(id: Int, title: String)
case class Anchor(surface: String, title: String)
case class Link(source_id: Int, target_title: String)
case class PageInfo(entity: Entity, anchors: List[Anchor], links: List[Link])

object Extractor {

  def extractEntityInfo(page: Elem) = {
    Entity(
      (page \ "id").head.text.toInt,
      (page \ "title").head.text
    )
  }

  def extractPageInfo(page: Elem): PageInfo = {
    val anchors = extractAnchors(page)
    val entity = extractEntityInfo(page)
    val links = packLinks(entity, anchors)
    PageInfo(entity, anchors, links)
  }

  def packLinks(entity: Entity, anchors: List[Anchor]): List[Link] = {
    anchors.map { anchor =>
      Link(entity.id, anchor.title)
    }
  }

  def extractAnchors(page: Elem) : List[Anchor] = {
    // Extract anchors given a XML page
    val text = (page \\ "text").head.text

    val link = """\[\[([^\[\]]+?)(?:\|([^\[\]]+))?\]\]""".r

    (link findAllIn text).map( _ match {
      case link(title, null) => Anchor(title, title)
      case link(surface, title) => Anchor(surface, title)
    }).toList
  }
}
