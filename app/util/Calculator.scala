package util

/**
 * User: max
 * Date: 12/21/12
 * Time: 12:08 PM
 */

import scala.BigDecimal.RoundingMode.HALF_UP

object Calculator {
  var KURS_DOLLARA: BigDecimal = 32
  var NAKRUTKA: BigDecimal = 30


  def calculateFinalPrice(original: BigDecimal): BigDecimal = {
    val result: BigDecimal = KURS_DOLLARA * ((original / 100 * NAKRUTKA) + original)
    result.setScale(2, HALF_UP)
  }
}
