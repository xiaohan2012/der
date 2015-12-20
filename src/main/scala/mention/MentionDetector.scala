package org.hxiao.der.mention

import scala.collection.JavaConversions.{mapAsJavaMap, asScalaIterator, asJavaIterator, asScalaSet}

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.common.params.{CommonParams, MapSolrParams}

import com.aliasi.chunk.{Chunk, Chunker}
import com.aliasi.dict.{DictionaryEntry, MapDictionary, ExactDictionaryChunker}
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory

import org.hxiao.der.util.SolrUtility


class SolrMapDictionary(
  val solr: EmbeddedSolrServer, val nrows: Int)
    extends MapDictionary[String] {

  override def addEntry(entry: DictionaryEntry[String]) = {}
  
  override def iterator():
      java.util.Iterator[DictionaryEntry[String]] = {
    phraseEntryIt("*:*")
  }
  
  override def phraseEntryIt(phrase: String):
      java.util.Iterator[DictionaryEntry[String]] = {
    val params = new MapSolrParams(Map(
      CommonParams.Q -> phrase,
      // CommonParams.FQ -> ("nercat:" + category),
      // CommonParams.FL -> "nerval",
      CommonParams.START -> "0",
      CommonParams.ROWS -> String.valueOf(nrows)))
    val rsp = solr.query(params)
    rsp.getResults().iterator().toList.map(doc =>  new DictionaryEntry[String](
      doc.getFieldValue("surface_name").asInstanceOf[String],
      "",
      doc.getFieldValue("log_occurrences").asInstanceOf[Double])).
      iterator
  }
}

class DictionaryMentionDetector(dict: MapDictionary[String]){
  private val chunker = new ExactDictionaryChunker(dict,
    IndoEuropeanTokenizerFactory.INSTANCE,
    false, false)

  def detect(text: String): List[Chunk] =
    chunker.chunk(text).chunkSet.toList


  def mkString(text: String, chunks: List[Chunk]): List[String] = 
    chunks.map {chunk => text.substring(chunk.start(), chunk.end())}

}

object DictionaryMentionDetector{
  def main(args: Array[String]) = {
    val text = args(0)

    val solr_dir = "/cs/home/hxiao/solr"
    val solr_core_name = "surface_names"
    val solr_server = SolrUtility.createEmbeddedSolrServer(solr_dir, solr_core_name)
    val dict = new SolrMapDictionary(solr_server, 100)
    val detector = new DictionaryMentionDetector(dict)
    val chunks = detector.detect(text)

    println(detector.mkString(text, chunks).sorted)
	
  }
}
