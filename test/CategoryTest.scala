import collection.immutable.{ListMap, SortedMap}
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
      val groupedBrands = util.Filter.collectAllBrands().groupBy(w => w).mapValues(_.size)
      println(groupedBrands.mkString("\n"))

      println("---------------------------------------")

      val groupedCategories =   ListMap((util.Filter.collectAllCategories().groupBy(w => w).mapValues(_.size).toList.sorted):_*)

      println(groupedCategories.mkString("\n"))

      assert(1 === 1)
    }
  }

}
