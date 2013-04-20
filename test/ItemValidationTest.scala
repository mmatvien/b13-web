import org.scalatest.FunSuite
import persistence.CartItemVariation
import play.api.test.FakeApplication
import play.api.test.Helpers._

/**
 * User: max
 * Date: 3/3/13
 * Time: 8:34 PM
 */
class ItemValidationTest extends FunSuite {

  test("Item should be available with the specific variation set") {
    running(FakeApplication()) {
      val variations = List(CartItemVariation("Size (Women's)", "S"), CartItemVariation("Color", "Pinks"),
        CartItemVariation("Shade", "PINK PULSE"))

      val variationsBad = List(CartItemVariation("Size (Women's)", "Z"), CartItemVariation("Color", "Pinks"),
        CartItemVariation("Shade", "PINK PULSE"))

      val itemGood = services.EbayService.checkLineItemWithVendor(3,"121061229672", variations)
      val itemBad = services.EbayService.checkLineItemWithVendor(2, "121061229672", variationsBad)
      assert(itemGood._1)
      assert(!itemBad._1)
    }
  }
}
