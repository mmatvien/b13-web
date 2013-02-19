/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

package util

import play.api.libs.json._
import persistence.VariationSpecifics

/**
 * User: max
 * Date: 1/11/13
 * Time: 10:18 AM
 */
object UI {

  def scaleEbayImage(url: String): String = {
    val res = url.split(".JPG").toList match {
      case spl :: rest if (!rest.isEmpty) => {
        spl.substring(0, spl.indexOf("~~")) + "~~60_10.JPG" + rest.mkString("")
      }
      case _ => url
    }
    res
  }

  def constructVariationDropdowns(specifics: List[VariationSpecifics]): Map[String, List[String]] = {

    def extractTup(spec: List[Map[String, String]]): List[(String, String)] = {
      (0 to spec.size - 1).map {
        index =>
          extract(index, spec)
      }.toList
    }


    def extract(index: Int, specs: List[Map[String, String]]): (String, String) = {
      val json = Json.parse("" + specs(index))
      ((json \ "name").as[String], (json \ "value").as[String])
    }


    var finalMap: Map[String, List[String]] = Map()

    specifics.filter(d => d.quantity > 0).foreach {
      spec =>
        val tupList = extractTup(spec.specific)

        for (tup <- tupList) {
          val list = tup._2.replace(",", "`") :: finalMap.getOrElse(tup._1, List())
          finalMap += tup._1 -> list.toSet.toList
        }

    }
    finalMap
  }


  def extractVariationSpecs(specs: List[Map[String, String]]): List[(String, String)] = {

    def extract(index: Int): (String, String) = {
      val json = Json.parse("" + specs(index))
      ((json \ "name").as[String], (json \ "value").as[String].replace(",", "`"))
    }
    (0 to specs.size - 1).foldLeft(Nil: List[(String, String)])((c, i) => extract(i) :: c)
  }


  def picExists(sp: List[VariationSpecifics], pic: String): Boolean = {
    def exists(spec: VariationSpecifics): List[Int] = {
      extractVariationSpecs(spec.specific.toList).map {
        x =>
          if (x._2.replaceAll(" ", "_") == pic) 1
          else 0
      }
    }
    val x = sp.map(d => exists(d)).flatten
    x.contains(1)
  }

  def compressCategory(category: String): String = {
    def normalize(str: String): String = {
      str.replaceAll(" ", "").replaceAll("&", "_").replaceAll("'", "_").replace("(", "_").replace(")", "_").replace(",", "_")
    }
    val pieces = category.split(":").toList
    if (pieces.size > 1) {
      normalize(pieces.reverse(1)) + "_" + normalize(pieces.reverse(0))
    } else {
      normalize(pieces(0))
    }

  }

  def generateShippingOptions(cartItems: List[persistence.CartItem]): List[(String, String)] = {
    val so = Calculator.calculateShipment(cartItems)
    val weight = Calculator.calculateShipment(cartItems).totalWeight

    var options: List[(String, String)] = Nil
    if (so.envelopeFit && weight < 1814.369) options = (f"envelope:${(24.95 * Calculator.KURS_DOLLARA)}%9.2f", "Priority Mail Flate Rate конверт") :: options
    else if (so.smallBoxFit && weight < 1814.369) options = (f"smallBox:${(24.95 * Calculator.KURS_DOLLARA)}%9.2f", "Priority Mail Flate Rate малый бокс") :: options
    else if (so.mediumBoxFit && weight < 9071.85) options = (f"mediumBox:${(60.95 * Calculator.KURS_DOLLARA)}%9.2f", "Priority Mail Flate Rate средний бокс") :: options
    else if (so.largeBoxFit  && weight < 9071.85) options = (f"bigBox:${(78.95 * Calculator.KURS_DOLLARA)}%9.2f", "Priority Mail Flate Rate большой бокс") :: options


    if (weight < 1814.369)
      options = (f"firstClass:${Calculator.shippingCostFirstClassMail(weight)}%9.2f", "First class mail (без отслеживания)") :: options
    if (weight < 19958.049)
      options = (f"priority:${Calculator.shippingCostPriorityMail(weight)}%9.2f", "Priority mail") :: options

    options
  }
}
