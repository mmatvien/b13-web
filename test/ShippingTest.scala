/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

import org.scalatest.FunSuite
import persistence._
import play.api.test.FakeApplication
import play.api.test.Helpers.running
import util.Calculator.ShipmentItemInfo
import util.Calculator.ShipmentItemEmpty
import util.FileUtil

/**
 * User: max
 * Date: 2/5/13
 * Time: 1:37 PM
 */


class ShippingTest extends FunSuite {

  val sellerList = List(
    "aeropostale"
  )

  test("Item should have a non-zero shipping cost") {

    running(FakeApplication()) {
      var failedCategories: List[String] = List()
      sellerList.foreach {
        seller =>
          FileUtil.removeFile(s"test/$seller")
          Item.findAllSellerItems(seller).map {
            item =>
              util.Calculator.generateItemShipmentInfo(item) match {

                case si:ShipmentItemInfo => {
//                  println(s"success ${item.categoryName}")
                }
                case ShipmentItemEmpty => {
                  if (!failedCategories.contains(item.categoryName)) {
                    util.FileUtil.appendToFile(s"test/$seller", item.categoryName)
                    failedCategories = item.categoryName :: failedCategories
                  }
                }
              }
          }
      }
      val failed = failedCategories.toSet.mkString("\n")
      print(failed)
      assert(failedCategories.isEmpty)

    }
  }

}
