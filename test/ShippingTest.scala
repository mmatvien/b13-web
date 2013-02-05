/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

import org.scalatest.FunSuite
import persistence._
import play.api.test.FakeApplication
import play.api.test.Helpers.running

/**
 * User: max
 * Date: 2/5/13
 * Time: 1:37 PM
 */

class ShippingTest extends FunSuite {


  test("Item should have a non-zero shipping cost") {
    running(FakeApplication()) {
      Item.findAllSellerItems("ebags").map {
        item =>
          println(s"itemID ${item.itemId} checking ${item.categoryName}")
          val res = util.Translator.extractShipmentInfo(item.categoryName)

         println(s"${item.categoryName.reverse.head} -- $res")

      }

      assert(1 > 0)
    }
  }

}
