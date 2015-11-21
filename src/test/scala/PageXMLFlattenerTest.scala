import scala.io.Source
import java.io.File
import org.hxiao.der.util.PageXMLFlattener
import org.scalatest.FlatSpec
import org.scalatest.Assertions._

class PageXMLFlattenerTest extends FlatSpec {
  "A PageXMLFlattener" should "extract page xml, flatten each page into one line and write them into a single file  " in {
    val inputPath: String = getClass.getResource("page-example-multiple.xml").getPath()
    val outputPath: String = "page-example-multiple-flattened.xml"
    PageXMLFlattener.process(inputPath, outputPath)

    assertResult(3){
      Source.fromFile(outputPath).getLines().size
    }
    val expectedLines: List[String] = List(
      "  <page>\t    <title>A</title>  </page>",
      "  <page>\t    <title>B</title>  </page>",
      "  <page>\t    <title>C</title>  </page>")
    (Source.fromFile(outputPath).getLines.toList zip expectedLines) foreach {
      case (actualLine, expectedLine) => {
        assertResult(expectedLine) {
          actualLine
        }
      }
    }
    val outputFile = new File(outputPath)
    outputFile.delete()
  }
}
