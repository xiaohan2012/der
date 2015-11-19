package org.hxiao.wikipedia

import scala.xml.XML

object LinkExtractor {
  
  def extract(pagePath: String) : Iterator[Tuple2[String, String]] = {
    // Extract links given a XML page
    val file = XML.loadFile(pagePath)
    val text = (file \\ "text").head.text

    val link = """\[\[([^\[\]]+?)(?:\|([^\[\]]+))?\]\]""".r

    val links = (link findAllIn text).map( _ match {
      case link(title, null) => (title, title)
      case link(surface, title) => (surface, title)
    })

    return links
  }
}
