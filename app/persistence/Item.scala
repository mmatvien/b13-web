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
                      pictures: List[VariationPictures],
                      specificsSet: Map[String, List[String]],
                      specifics: List[VariationSpecifics])

case class VariationSpecifics(
                               quantity: Int,
                               specific: List[Map[String, String]]
                               )


case class VariationPictures(name: String, url: String)

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
  def findSellerItems(seller: String, cat: String, filter: String, size: String, page: Int): List[Item] = {
    val itemsPerPage = 30
    dao.find(collectionQuery(seller, cat, filter)).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).toList
  }


  def findByCategory(cat: String, filter: String, size: String, page: Int): List[Item] = {
    val itemsPerPage = 30
    val quer = categoryQuery(cat, filter)
    println(quer)
    dao.find(categoryQuery(cat, filter)).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).toList
  }

  def collectionQuery(seller: String, cat: String, filter: String): MongoDBObject = {
    val queryBuilder = MongoDBObject.newBuilder
    queryBuilder += "collection" -> seller
    queryBuilder += "categoryName" -> (".*" + cat + ".*" + filter + ".*").r
    queryBuilder.result()
  }


  def categoryQuery(cat: String, filter: String): MongoDBObject = {
    val queryBuilder = MongoDBObject.newBuilder
    queryBuilder += "categoryName" -> (".*(?i):" + cat + ".*" + filter + ".*").r
    queryBuilder.result()
  }

  def findSellerPagerSize(seller: String, cat: String, filter: String, size: String): Int = {
    dao.count(collectionQuery(seller, cat, filter)).toInt
  }

  def findCategoryPagerSize( cat: String, filter: String, size: String): Int = {
    dao.count(categoryQuery(cat, filter)).toInt
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