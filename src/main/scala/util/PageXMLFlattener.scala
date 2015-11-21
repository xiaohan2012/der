package org.hxiao.der.util
import scala.io.Source
import java.io.{BufferedWriter, FileWriter}

object PageXMLFlattener{
  val startTag = "<page>"
  val endTag = "</page>"
  def process(inputPath: String, outputPath: String) = {
    val outputFile = new BufferedWriter(new FileWriter(outputPath))
    Source.fromFile(inputPath).getLines.foldLeft("") { (acc, line) =>
      val strippedLine = line.replaceAll("""^\s+""", "")  // remove heading space
      if (strippedLine.startsWith(startTag)) {
        line
      }
      else if (strippedLine.startsWith(endTag)) {
        outputFile.write(acc + line + '\n')
        ""
      }
      else{
        acc + "\t" + line
      }
    }
    outputFile.close()
  }

  def main(args: Array[String]) = {
    process(args(0), args(1))
  }
}
