/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

package persistence

import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import org.joda.time.DateTime
import com.mongodb.casbah.commons.MongoDBObject

/**
 * User: max
 * Date: 1/29/13
 * Time: 1:22 PM
 */

case class Ops(
                seller: String,
                lastRan: DateTime,
                removed: Int,
                updated: Int,
                added: Int,
                other: Int,
                size: Int
                )

object Ops extends ModelCompanion[Ops, ObjectId] {

  val collection = MongoConnection()("b13_ebay")("ops")
  val dao = new SalatDAO[Ops, ObjectId](collection = collection) {}

  def getLatest: List[Ops] = {
    val sort = MongoDBObject("lastRan" -> -1)
    dao.find(MongoDBObject.empty).sort(sort).toList.groupBy{_.seller}.map{_._2.head}.toList
  }


}
