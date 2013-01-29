package persistence

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._
import java.util.Date
import java.text.SimpleDateFormat
import org.joda.time.DateTime

/**
 * User: max
 * Date: 12/17/12
 * Time: 9:10 PM
 */
object Util {
  def listU(li: String): List[String] = {
    val one = li.dropRight(1).drop(1)

    val arr = one.split(",")
    val result = arr.foldLeft(List(): List[String])((i, s) =>
      s.trim.toList.filter(x => x != '\"').mkString("") :: i
    )

    result
  }

  def formatDate(dt: Date): String = {
    val format: SimpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm")
    format.format(dt)
  }

  def formatDateTime(dt: DateTime): String = dt.toString("MM-dd-yyyy HH:mm")

  def collectCategories(collection: String): List[Map[String, List[Map[String, List[String]]]]] = {

    def appendCat(cat: String, s: List[Map[String, List[Map[String, List[String]]]]], key: String, res: Map[String, List[String]]): List[Map[String, List[Map[String, List[String]]]]] = {
      val existing = s.filter(ll => ll.contains(cat))
      val init = if (existing.isEmpty) List() else existing.head(cat)

      val newMap = Map(key -> res(key))
      Map(cat -> (newMap :: init)) :: s.filter(ll => !ll.contains(cat))
    }

    lazy val categories: List[Map[String, List[Map[String, List[String]]]]] = {
      val q = MongoDBObject("collection" -> collection)
      val fields = MongoDBObject("categoryName" -> 1)
      val cats = MongoFactory.connection_b13_ebay.find(q, fields).toList.map {
        x => {
          val all = x("categoryName").toString.split(":").toList
          all.drop(1).head :: all.drop(1).tail
        }
      }

      val grouped = cats.groupBy(_.head)

      val res = grouped.keys.foldLeft(Map(): Map[String, List[String]])((s, i) =>
        s ++ Map(i -> grouped(i).flatten.toSet.filter(xs => xs != i).toList)
      )

      val formatted2 = res.keys.foldLeft(List(): List[Map[String, List[Map[String, List[String]]]]])((s, key) =>

        if (key.contains("Women")) appendCat("she", s, key, res)
        else if (key.contains("Men")) appendCat("he", s, key, res)
        else if (key.contains("Kid")) appendCat("kid", s, key, res)
        else appendCat("other", s, key, res)

      )

      formatted2
    }

    categories
  }

  case class Specifics(name: String, value: String)

  def collectSpecifics(collection: String): Set[Any] = {
    val q = MongoDBObject("collection" -> collection)
    val fields = MongoDBObject("specifics" -> 1)
    val specifics = MongoFactory.connection_b13_ebay.find(q, fields).toList.map {
      x => {
        x.getAs[DBObject]("specifics") match {
          case Some(sp) => sp.toMap.get("Brand")
          case None => ""
        }
      }
    }
    specifics.toSet
  }
}
