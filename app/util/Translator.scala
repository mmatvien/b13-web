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

}
