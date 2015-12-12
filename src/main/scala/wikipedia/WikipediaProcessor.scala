package org.hxiao.der.wikipedia

import scala.xml.XML
import org.apache.spark
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
// import org.apache.spark.sql.SQLContext

import scala.collection.Map
import scala.collection.immutable.HashMap
import scala.collection.mutable.{HashMap => MHashMap}


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

  def collectPageInfo(sc: SparkContext, xml_path: String, min_partitions: Int): RDD[RawPageInfo] = {
    sc.textFile(xml_path, min_partitions).map {
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

  def normalizeLinks(raw_links: RDD[RawLink], title2id: Broadcast[Map[String, Int]]): RDD[Link] = 
    normalizeLinks(raw_links, title2id.value)

  def normalizeAnchors(raw_anchors: RDD[RawAnchor], title2id: Map[String, Int]): RDD[Anchor] = {
    raw_anchors.map {
      a => Anchor(
        a.surface,
        title2id.getOrElse(a.title, NON_EXIST_ENTITY_ID)
      )
    }
  }
  def normalizeAnchors(raw_anchors: RDD[RawAnchor], title2id: Broadcast[Map[String, Int]]): RDD[Anchor] = 
    normalizeAnchors(raw_anchors, title2id.value)
  
  def collectSurfaceNames(anchors: RDD[Anchor], ignoreTable: Boolean = true): RDD[SurfaceName] = {
    anchors.map(
      a => (a.surface, a.id)
    ).aggregateByKey(
      MHashMap[EntityID, Int]()
    )((table, id) => {
      table.put(id, table.getOrElse(id, 0) + 1)
      table
      },
      (t1, t2) => {
        t1 ++ t2.map { case(k, v) => (k, v + t1.getOrElse(k, 0))
        }
      }
    ).map {
      case (surface, tbl) => {
        if(ignoreTable)
          new SurfaceName(surface, null, tbl.values.sum)
        else
          new SurfaceName(surface, tbl.toSeq)
      }
    }
  }

  // return:
  // 1. title2id mapping
  // 2. links
  // 3. surface2entity frequency
  def apply(sc: SparkContext, xml_path: String, ignoreTable: Boolean=false): (Map[String, Int], RDD[Link], RDD[SurfaceName]) = {
    val pageInfo = collectPageInfo(sc, xml_path, sc.defaultMinPartitions)
    val raw_links = collectLinks(pageInfo)
    val raw_anchors = collectAnchors(pageInfo)
    val title2id = sc.broadcast(collectTitle2Id(pageInfo).collectAsMap())

    // is title2id computed twice?
    // maybe title2id should be cached using `persist()`
    // `persist` only works for RDD
    // maybe `broadcast()`?
    // http://spark.apache.org/docs/latest/programming-guide.html#basics

    val links = normalizeLinks(raw_links, title2id)
    val anchors = WikipediaProcessor.normalizeAnchors(raw_anchors, title2id)
    val surface_names = WikipediaProcessor.collectSurfaceNames(anchors, ignoreTable)

    return (title2id.value, links, surface_names)
  }

  def extractSurfaceNames(sc: SparkContext, xml_path: String, min_partitions: Int, ignoreTable: Boolean=false): RDD[SurfaceName] = {
    val pageInfo = collectPageInfo(sc, xml_path, min_partitions)
    val raw_anchors = collectAnchors(pageInfo)
    val title2id = collectTitle2Id(pageInfo).collectAsMap()
    val title2id_broadcast = sc.broadcast(title2id)
    val anchors = WikipediaProcessor.normalizeAnchors(raw_anchors, title2id_broadcast)
    WikipediaProcessor.collectSurfaceNames(anchors, ignoreTable=ignoreTable)
  }
}

