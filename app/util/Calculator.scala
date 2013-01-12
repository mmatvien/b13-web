package util

/**
 * User: max
 * Date: 12/21/12
 * Time: 12:08 PM
 */

import scala.BigDecimal.RoundingMode.HALF_UP
import persistence.CartItem

object Calculator {
  var KURS_DOLLARA: BigDecimal = 32


  def calculateFinalPrice(original: BigDecimal): BigDecimal = {
    val commission = calculateCommissionPercentage(original)
    val commissionDollars = (original / 100 * commission)
    val result: BigDecimal = KURS_DOLLARA * (commissionDollars + original)
    result.setScale(2, HALF_UP)
  }

  def calculateCommissionPercentage(original: BigDecimal): Int = {
    if (original < 50) return 30
    if (original < 100) return 25
    if (original < 300) return 20
    13
  }

  case class ShipmentTotalOption(totalWeight: BigDecimal,
                                 envelopeFit: Boolean,
                                 mediumBoxFit: Boolean,
                                 largeBoxFit: Boolean)

  case class ShipmentItemOption(weight: BigDecimal,
                                envelopeFit: Boolean,
                                mediumBoxFit: Int,
                                largeBoxFit: Int)


  def getWeight(shippingStructure: List[String], subcategory: String): BigDecimal = {

    println(subcategory)

    val s: BigDecimal = {
      if (shippingStructure.size > 1) {
        if (subcategory.contains("Men")) BigDecimal(shippingStructure(0))
        else if (subcategory.contains("Women")) BigDecimal(shippingStructure(1))
        else if (subcategory.contains("Kid") || subcategory.contains("Boy") || subcategory.contains("Girl")) BigDecimal(shippingStructure(2))
        else BigDecimal(0)
      } else BigDecimal(0)
    }
    s
  }

  def calculateShipment(cartItems: List[CartItem]) {
    val cartShipmentOptions = cartItems.foldLeft(Nil: List[ShipmentItemOption]) {
      (sum, cartItem) =>
        val item = persistence.Item.getItem(cartItem.itemId)
        val fullList = item.categoryName.split(':').toList.reverse
        val shipping = Translator.matchFromFile("conf/shipping.txt", fullList.head)
        val shippingStructureFull = shipping.split(',').toList
        val subcategory = fullList.tail.head


        val itemWeight = getWeight(shippingStructureFull.drop(0), subcategory)
        val envelope = getWeight(shippingStructureFull.drop(3), subcategory)
        val mediumBoxPercentage = getWeight(shippingStructureFull.drop(6), subcategory)
        val largeBoxPercentage = getWeight(shippingStructureFull.drop(9), subcategory)

        val itemShipmentOption = ShipmentItemOption(itemWeight, envelope == 0, mediumBoxPercentage.toInt, largeBoxPercentage.toInt)
        itemShipmentOption :: sum
    }

    val totalWeight = cartShipmentOptions.map(x => x.weight).sum
    val envelopeFit = {
      if (cartShipmentOptions.size > 1) false
      else if (cartShipmentOptions(0).envelopeFit == 0) true
      else false
    }

    val mediumBoxFit = {
      if (cartShipmentOptions.map(x => x.mediumBoxFit).sum > 100) false
      else true
    }

    val largeBoxFit = {
      if (cartShipmentOptions.map(x => x.largeBoxFit).sum > 100) false
      else true
    }

    val shipmentTotalOption = ShipmentTotalOption(totalWeight, envelopeFit, mediumBoxFit, largeBoxFit)

    println(cartShipmentOptions)

    println(shipmentTotalOption)

  }


}
