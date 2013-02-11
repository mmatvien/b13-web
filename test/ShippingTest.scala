/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

import org.scalatest.FunSuite
import persistence._
import play.api.test.FakeApplication
import play.api.test.Helpers.running
import util.FileUtil

/**
 * User: max
 * Date: 2/5/13
 * Time: 1:37 PM
 */


class ShippingTest extends FunSuite {

  val sellerList = List(
    "tommyhilfiger",
    "aeropostale",
    "watchgrabber",
    "shopwss",
    "reedsjewelers"
  )

  test("Item should have a non-zero shipping cost") {

    running(FakeApplication()) {
      var failedCategories: List[String] = List()
      sellerList.foreach {
        seller =>
          FileUtil.removeFile(s"test/$seller")
          Item.findAllSellerItems(seller).map {
            item =>
              val res = util.Translator.extractShipmentInfo(item.categoryName).toInt
              if (!shippingExists(res) && !failedCategories.contains(item.categoryName)) {
                util.FileUtil.appendToFile(s"test/$seller", item.categoryName)
                failedCategories = item.categoryName :: failedCategories
              }
          }
      }
      assert(failedCategories.isEmpty)
      def shippingExists(v: Int): Boolean = v > 0
    }
  }


}
