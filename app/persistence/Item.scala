/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

package persistence

import com.mongodb.casbah.Imports._
import com.novus.salat.dao._


/**
 * User: max
 * Date: 12/16/12
 * Time: 9:57 PM
 */
case class Variation(
                      pictures: List[String],
                      specificsSet: Map[String, List[String]],
                      specifics: List[VariationSpecifics])

case class VariationSpecifics(
                               quantity: Int,
                               specific: List[Map[String, String]]
                               )

case class VSMap(map: Map[String, String])

case class Item(
                 collection: String,
                 itemId: String,
                 itemUrl: String,
                 title: String,
                 categoryID: String,
                 categoryName: String,
                 price: Double,
                 originalPrice: Double,
                 mainPicture: String,
                 variations: Variation,
                 specifics: Map[String, String],
                 state: Int)

object Item extends ModelCompanion[Item, ObjectId] {

  val coll = MongoConnection()("b13_ebay")("items")
  val dao = new SalatDAO[Item, ObjectId](collection = coll) {}


  // -- Queries
  def findItems(collection: String, cat: String, filter: String, size: String, page: Int): List[Item] = {
    val itemsPerPage = 30
    dao.find(buildQuery(collection, cat, filter)).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).toList
  }


  def buildQuery(collection: String, cat: String, filter: String): MongoDBObject = {
    val queryBuilder = MongoDBObject.newBuilder
    queryBuilder += "collection" -> collection
    queryBuilder += "categoryName" -> (".*" + cat + ".*" + filter + ".*").r
    queryBuilder.result()
  }

  def findPagerSize(collection: String, cat: String, filter: String, size: String): Int = {
    dao.count(buildQuery(collection, cat, filter)).toInt
  }


  def changeState(itemId: String, state: Int) {
    val item = getItem(itemId)
    dao.update(q = MongoDBObject("itemId" -> itemId),
      t = item.copy(state = state),
      upsert = false, multi = false, wc = Item.dao.collection.writeConcern)
  }


  def getItem(itemId: String): Item = {
    dao.findOne(MongoDBObject("itemId" -> itemId)).get
  }

}