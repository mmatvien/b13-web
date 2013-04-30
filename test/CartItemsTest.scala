import org.scalatest.FunSuite
import play.api.test.FakeApplication
import play.api.test.Helpers._

/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

/**
 * User: max
 * Date: 4/28/13
 * Time: 5:06 PM
 */
class CartItemsTest
  extends FunSuite {

  test("Add new fields to the Cart Itmes") {
    running(FakeApplication()) {
      persistence.Cart.findAll().toList.map{ cart =>
        println(cart.sessionId)
      }
    }
  }
}
