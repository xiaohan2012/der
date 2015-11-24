package org.hxiao.der.wikipedia



package object classes {
  import scala.math.Ordered.orderingToOrdered
  type EntityID = Int

  case class Entity(id: EntityID, title: String)

  case class RawAnchor(surface: String, title: String) extends Ordered[RawAnchor]{
    def compare(that: RawAnchor): Int = (this.surface, this.title) compare (that.surface, that.title)
  }

  case class RawLink(source_id: EntityID, target_title: String) extends Ordered[RawLink]{
    def compare(that: RawLink): Int = (this.source_id, this.target_title) compare (that.source_id, that.target_title)
  }

  case class RawPageInfo(entity: Entity, anchors: List[RawAnchor], links: List[RawLink])

  case class Anchor(surface: String, id: EntityID) extends Ordered[Anchor]{
    def compare(that: Anchor): Int = (this.surface, this.id) compare (that.surface, that.id)
  }

  case class Link(source_id: EntityID, target_id: EntityID) extends Ordered[Link]{
    def compare(that: Link): Int = (this.source_id, this.target_id) compare (that.source_id, that.target_id)
  }
}
