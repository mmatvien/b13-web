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


  def extractVariationSpecs(specs: List[Map[String, String]]): List[(String, String)] = {
    def extract(index: Int): (String, String) = {
      val json = Json.parse("" + specs(index))
      ((json \ "name").as[String], (json \ "value").as[String])
    }
    println(specs)
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
      str.replaceAll(" ", "").replaceAll("&", "_").replaceAll("'", "_").replace("(", "_").replace(")", "_").replace(",","_")
    }
    val pieces = category.split(":").toList
    if (pieces.size > 1) {
      normalize(pieces.reverse(1)) + "_" + normalize(pieces.reverse(0))
    } else {
      normalize(pieces(0))
    }

  }

}
