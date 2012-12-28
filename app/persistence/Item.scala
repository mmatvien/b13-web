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
                 collection:String,
                 itemId: String,
                 itemUrl: String,
                 title: String,
                 categoryID: String,
                 categoryName: String,
                 price: Double,
                 originalPrice: Double,
                 mainPicture: String,
                 variations: Variation,
                 specifics: Map[String, String])

class CollectionM(collection: String) {

  object Item extends ModelCompanion[Item, ObjectId] {

    val coll = MongoConnection()("b13_ebay")(collection)
    val dao = new SalatDAO[Item, ObjectId](collection = coll) {}


    // -- Queries
    def findItems(cat: String, filter: String, size: String): List[Item] = {
      val unfiltered = dao.find(MongoDBObject()).toList

      val filtered = if (filter != "") {
        unfiltered.filter {
          item =>
            item.categoryName.contains(cat) && item.categoryName.toUpperCase.contains(filter.toUpperCase)
        }
      } else unfiltered

      val sized = if (size != "") {
        filtered.filter {
          item =>
            val sizes = for (
              key <- item.variations.variations.keys
              if (key.contains("Size"))
            ) yield {
              val x1 = "" + item.variations.variations(key)
              Util.listU(x1).exists(x => x == size)
            }

            // println(item.itemId + " : " +sizes.headOption.getOrElse(false) )

            sizes.headOption.getOrElse(false)

        }
      } else filtered

      sized
      //   unfiltered
    }

    def getItem(itemId: String): Item = {
      dao.findOne(MongoDBObject("itemId" -> itemId)).get
    }
  }

}