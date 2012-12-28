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
  def translate(original: String): String = {

    val res = for (v <- Source.fromFile("conf/dictionary.txt").getLines().toList
                   if (v.toString.split('=')(0) == original)
    ) yield v.toString.split('=')(1).toString

    if (res.isEmpty) original
    else res.head

  }
}
