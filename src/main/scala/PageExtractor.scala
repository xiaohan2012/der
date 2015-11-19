import scala.xml.XML

object PageExtractor{

  val file = XML.loadFile("page-example.xml")
  val text = (file \\ "text").head.text

  val link = """\[\[([^\[\]]+?)(?:\|([^\[\]]+))?\]\]""".r

  val links = (link findAllIn text).map( _ match {
    case link(title, null) => (title, title)
    case link(surface, title) => (surface, title)
  })

  // (Category:Campinas, ) dead link

  links.foreach(a => {
    println(a._1, a._2)
  })
}


object Foo{
  def Bar: List[Int] = 
    List(1, 2, 3, 4).map(_ * 4)

}
