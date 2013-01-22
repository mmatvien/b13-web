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
    (0 to specs.size - 1).foldLeft(Nil: List[(String, String)])((c, i) => extract(i) :: c)
  }


  def picExists(sp: List[VariationSpecifics], pic: String): Boolean = {
    def exists(spec: VariationSpecifics): List[Int] = {
      extractVariationSpecs(spec.specific.toList).map {
        x =>
          println("comparing " + x._2 + " to " + pic)
          if (x._2 == pic) 1
          else 0
      }
    }
    val x = sp.map(d => exists(d)).flatten
    x.contains(1)
  }
}
