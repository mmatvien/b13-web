/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

package util

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
}
