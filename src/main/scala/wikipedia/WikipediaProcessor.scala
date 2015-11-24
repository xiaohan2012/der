package org.hxiao.der.wikipedia

import scala.xml.XML
import org.apache.spark
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
// import org.apache.spark.sql.SQLContext

import scala.collection.Map
import scala.collection.immutable.HashMap


import org.hxiao.der.wikipedia.classes._

// Things to collect:

// The links between articles: List[Tuple2[String, String]]
// anchors between surface and article: List[Tuple2[String, String]]
// Article content: HashMap[Int, List[String]]

// Some mapping:
// Article name to id: HashMap[String, Int]
// Token name to id: HashMap[String, Int]

object WikipediaProcessor {
  val NON_EXIST_ENTITY_ID = -1

  def collectPageInfo(sc: SparkContext, xml_path: String): RDD[RawPageInfo] = {
    sc.textFile(xml_path).map {
      XML.loadString(_)
    }.filter {
      page => (page  \ "redirect").length == 0  // not a redirect page
    }.map{
      Extractor extractPageInfo _
    }
  }

  def collectLinks(page_infos: RDD[RawPageInfo]): RDD[RawLink] = {
    page_infos.map(_.links).flatMap(identity).distinct()
  }

  def collectAnchors(page_infos: RDD[RawPageInfo]): RDD[RawAnchor] = {
    page_infos.map(_.anchors).flatMap(identity)
  }

  def collectTitle2Id(page_infos: RDD[RawPageInfo]): RDD[Tuple2[String, Int]]= {
    page_infos.map {
      p => (p.entity.title, p.entity.id)
    }
  }

  def normalizeLinks(raw_links: RDD[RawLink], title2id: Map[String, Int]): RDD[Link] = {
    raw_links.map {
      l => Link(
        l.source_id,
        title2id.getOrElse(l.target_title, NON_EXIST_ENTITY_ID)
      )
    }
  }

  def normalizeAnchors(raw_anchors: RDD[RawAnchor], title2id: Map[String, Int]): RDD[Anchor] = {
    raw_anchors.map {
      a => Anchor(
        a.surface,
        title2id.getOrElse(a.title, NON_EXIST_ENTITY_ID)
      )
    }
  }

  // def apply(xml_path: String, sc: SparkContext) = {
  //   val page_infos = sc.parallelize(spark.textFile(xml_path)).map (
  //     line => {
  //       val page = XML.loadString(line)
  //       Extractor.extractPageInfo(page)
  //     }
  //   ).collect()

  //   // build hash map from entity name to id
  //   val title2id: HashMap[String, Int] = page_infos.map {
  //     p => (p.entity.name, p.entity.id)
  //   }.toMap
      
  //   // get anchors, links(titles mapped to id), entity name to id mapping

  //   // unique links
  //   val links: List[Tuple2[Int, Int]] = page_infos.map {
  //     p => (p.link.source_id, title2id(p.link.target_title))
  //   }.distinct()

  //   // title to surface frequency
  //   val anchors: List[Tuple2[Int, HashMap[String, Int]]] = page_infos.map {
  //     p => (title2id(p.anchor.title), p.anchor.surface)
  //   }.groupByKey().map (
  //     case (id, surfaces) => {
  //       // count frequency of each surface
  //       val cnt = surfaces.map( s => (s, 1))
  //       cnt.reduceByKey(a,b => a+b)        
  //     }
  //   )


  //   // get frequency
  //   val commonness: HashMap[Int, HashMap[String, Int]] = {
  //   }

    


    // map the anchor strings to ids

    // save to database
    // - entity name -> id mapping
    // - links
    // - anchors(as well as count)
}

