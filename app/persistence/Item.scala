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

  // -- Queries
  def findAllItems(): List[Item] = {
    dao.find(MongoDBObject.empty).toList
  }

  def findItemsByBrand(brands: List[String]): List[Item] = {
    if (brands.isEmpty)
      dao.find(MongoDBObject.empty).toList
    else
      dao.find("specifics.Brand" $in brands).toList
  }

  def findAllSellerItems(seller: String): List[Item] = {
    dao.find(collectionQuery(seller, "", "")).toList
  }


  def findSellerItems(seller: String, cat: String, filter: String, size: String, page: Int): List[Item] = {
    val itemsPerPage = 30
    dao.find(collectionQuery(seller, cat, filter)).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).toList
  }


  def findByCategory(gender: String, cat: String, brand: String, filter: String, size: String, page: Int): List[Item] = {
    val itemsPerPage = 30
    val quer = categoryQuery(gender, cat, brand, filter)
    println(quer)
    dao.find(quer).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).toList
  }


  def categoryQuery(section: String, cat: String, brand: String, filter: String): MongoDBObject = {
    val queryBuilder = MongoDBObject.newBuilder
    if (cat.contains("Men")) {
      queryBuilder += "categoryName" -> (".*(?i):" + cat + ".*" + filter + ".*").r
    } else {
      queryBuilder += "categoryName" -> (".*(?i)" + cat + ".*" + filter + ".*").r
    }
    if (!brand.isEmpty)
      queryBuilder += "specifics.Brand" -> (".*(?i)" + brand + ".*").r

    if (cat.contains("Watches"))
      if (section.contains("she")) {
        val femaleList = List((".*Ladies.*").r, (".*Women.*").r)
        queryBuilder ++= "specifics.Gender" $in femaleList
      }
      else
        queryBuilder += "specifics.Gender" -> (".*Men.*").r
    queryBuilder.result()
  }


  def collectionQuery(seller: String, cat: String, filter: String): MongoDBObject = {
    val queryBuilder = MongoDBObject.newBuilder
    queryBuilder += "collection" -> seller
    queryBuilder += "categoryName" -> (".*" + cat + ".*" + filter + ".*").r
    queryBuilder.result()
  }

  def findSellerPagerSize(seller: String, cat: String, filter: String, size: String): Int = {
    dao.count(collectionQuery(seller, cat, filter)).toInt
  }

  def findCategoryPagerSize(gender: String, cat: String, brand: String, filter: String, size: String): Int = {
    dao.count(categoryQuery(gender, cat, brand, filter)).toInt
  }


  def changeState(itemId: String, state: Int) {
    val item = getItem(itemId)
    dao.update(q = MongoDBObject("itemId" -> itemId),
      t = item.copy(state = state),
      upsert = false, multi = false, wc = Item.dao.collection.writeConcern)
  }


  def findBrandsByCategory(gender: String, cat: String, filter: String): List[String] = {
    val quer = categoryQuery(gender, cat, "", filter)
    dao.find(quer).map(it => it.specifics.get("Brand").getOrElse("")).toSet.toList.sorted
  }

  def getItem(itemId: String): Item = {
    dao.findOne(MongoDBObject("itemId" -> itemId)).get
  }

}