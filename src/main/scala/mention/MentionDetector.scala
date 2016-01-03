package org.hxiao.der.mention

import scalaj.http.Http
import scala.xml.XML

case class Mention(id: String, start: Int, end: Int, text: String)

class MentionDetector(val service_url: String){
  val OK_CODE = 0

  def request(text: String): String = 
    Http(service_url)
      .method("POST")
      .header("Content-Type", "text/plain")
      .postData(text)
      .asString.body

  def parseResponse(res: String): Seq[Mention] = {
    val obj = XML.loadString(res)
    val code = ((obj \ "lst").head \ "int").head.text.toInt
    if(code != OK_CODE){
      throw new Exception(s"Error code $code.\n Full message:\n${obj}")
    }
    (obj \ "arr" \ "lst").map(
      e => {
        Mention(
          (e \ "arr" \ "str").head.text, // take the first matching surface name
          (e \ "int").head.text.toInt,
          (e \ "int").tail.text.toInt,
          (e \ "str").head.text
        )
      })
  }

  def detect(text: String): Seq[Mention] = 
    parseResponse(request(text))

}
