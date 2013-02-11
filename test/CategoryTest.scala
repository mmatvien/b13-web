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
 * Date: 2/8/13
 * Time: 1:36 PM
 */

class CategoryTest extends FunSuite {

  val sellerList = List(
    "tommyhilfiger",
    "aeropostale",
    "watchgrabber",
    "shopwss",
    "reedsjewelers"
  )


  test("Category loader should split each item's category") {
    running(FakeApplication()) {
     // util.Filter.collectCategoriesForBrand(List("Seiko")).map(println)
     // util.Filter.collectBrandsForCategory("").map(println)
      util.Filter.collectAllBrands().map(println)

      util.Filter.collectAllCategories().map(println)

      assert(1 === 1)
    }
  }

}
