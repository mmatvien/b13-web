/*
 * Copyright (c) 2012.
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
                      variations: Map[String, List[String]])


case class Item(
                 itemId: String,
                 itemUrl: String,
                 title: String,
                 categoryID: String,
                 price: Double,
                 originalPrice: Double,
                 mainPicture: String,
                 variations: Variation,
                 specifics: Map[String,String])

object Item extends ModelCompanion[Item, ObjectId] {

  val collection = MongoConnection()("b13_ebay")("guess_outlet")
  val dao = new SalatDAO[Item, ObjectId](collection = collection) {}

  // -- Queries
  def findItems(): List[Item] = dao.find(MongoDBObject()).toList

  def getItem(itemId: String): Item = {
    dao.findOne(MongoDBObject("itemId" -> itemId)).get
  }

}