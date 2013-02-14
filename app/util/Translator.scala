/*
 * Copyright (c) 2012.
 * All Right Reserved, http://www.maxiru.com/
 */

package util

import io.Source

/**
 * User: max
 * Date: 12/27/12
 * Time: 11:14 AM
 */
object Translator {


  /**
   * helper Function for matching words from files
   *
   * @param fileName file path
   * @param matcher word to match
   * @return matched word
   */
  def matchFromFile(fileName: String, matcher: String): String = {
    val res = for (
      v <- Source.fromFile(fileName).getLines().toList
      if (v.toString.split('=')(0) == matcher)
    ) yield v.toString.split('=')(1).toString
    res.toSet.mkString("")
  }

  /**
   * simple 1:1 translator
   *
   * @param original english word
   * @return translated word from dictionary
   */
  def translate(original: String): String = {
    val res = matchFromFile("conf/dictionary.txt", original)
    if (res.isEmpty) original
    else res
  }


  /**
   * shipping calculator
   *
   * @param category full category string
   */
  def extractShipmentInfo(category: String): Double = {

    val fullList = category.split(':').toList.reverse

    val shipping = matchFromFile("conf/shipping.txt", fullList.head)

    val shippingStructure = shipping.split(',').toList
    val parentCategory = fullList.tail.head


    def extract(parentCategory: String, shippingStructure: List[String]): Double = {
      if (shippingStructure.size > 1) {

        if (parentCategory.contains("Men")) {
          println("found men")
          return (shippingStructure(0).toDouble)
        } else if (parentCategory.contains("Women")) {
          println("found women")
          return (shippingStructure(1).toDouble)
        } else if (parentCategory.contains("Kid") || parentCategory.contains("Boy") || parentCategory.contains("Girl") || parentCategory.contains("Baby")) {
          println("found kid")
          return (shippingStructure(2).toDouble)
        } else 0
      } else {
        -1.toDouble
      }
    }
    extract(parentCategory, shippingStructure)
  }


}
