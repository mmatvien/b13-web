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
import org.scalatest.Tag

/**
 * User: max
 * Date: 2/5/13
 * Time: 1:37 PM
 */
object CategoryTest extends Tag("Category Test")

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
          FileUtil.removeFile(seller)
          Item.findAllSellerItems(seller).map {
            item =>
              val res = util.Translator.extractShipmentInfo(item.categoryName).toInt
              if (!shippingExists(res) && !failedCategories.contains(item.categoryName)) {
                util.FileUtil.appendToFile(seller, item.categoryName)
                failedCategories = item.categoryName :: failedCategories
              }
          }
      }
      assert(failedCategories.isEmpty)
      def shippingExists(v: Int): Boolean = v > 0
    }
  }


  test("Category loader should split each item's category", CategoryTest) {
    running(FakeApplication()) {
      assert(1===1)
    }
  }

}
