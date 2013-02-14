package util

/**
 * User: max
 * Date: 12/21/12
 * Time: 12:08 PM
 */

import scala.BigDecimal.RoundingMode.HALF_UP
import persistence.{Item, CartItem}

object Calculator {
  var KURS_DOLLARA: BigDecimal = 32


  def calculateFinalPrice(original: BigDecimal): BigDecimal = {
    val commissionPercent = calculateCommissionPercentage(original)
    val commissionDollars = (original / 100 * commissionPercent)
    val salesTax = original / 100 * 7
    val result: BigDecimal = KURS_DOLLARA * (commissionDollars + original + salesTax)
    val merchantFee = result / 100 * 3
    val finalResult = result + merchantFee

    finalResult.setScale(2, HALF_UP)
  }

  def calculateCommissionPercentage(original: BigDecimal): Int = {
    if (original < 50) return 30
    if (original < 100) return 25
    if (original < 300) return 20
    13
  }

  case class ShipmentTotalOption(totalWeight: BigDecimal,
                                 envelopeFit: Boolean,
                                 smallBoxFit: Boolean,
                                 mediumBoxFit: Boolean,
                                 largeBoxFit: Boolean) {
    override def toString = s"total weight = $totalWeight envelope fit = $envelopeFit : small box fit = $smallBoxFit : medium box fit = $mediumBoxFit : large box fit = $largeBoxFit"
  }

  trait ShipmentItem

  case class ShipmentItemInfo(weight: BigDecimal,
                              envelopeFit: Boolean,
                              smallBoxFit: Int,
                              mediumBoxFit: Int,
                              largeBoxFit: Int) extends ShipmentItem

  case object ShipmentItemEmpty extends ShipmentItem


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

  def generateItemShipmentInfo(item: Item): ShipmentItem = {
    val category = item.categoryName
    val shipping = Translator.matchFromFile("conf/shipping.txt", category)
    if (shipping.isEmpty)
      ShipmentItemEmpty
    else {
      val shippingStructureFull = shipping.split(',').toList

      val itemWeight = BigDecimal(shippingStructureFull(0))
      val envelope = BigDecimal(shippingStructureFull(1))
      val smallBoxPercentage = BigDecimal(shippingStructureFull(2))
      val mediumBoxPercentage = BigDecimal(shippingStructureFull(3))
      val largeBoxPercentage = BigDecimal(shippingStructureFull(4))

      ShipmentItemInfo(itemWeight, envelope == 0, smallBoxPercentage.toInt, mediumBoxPercentage.toInt, largeBoxPercentage.toInt)
    }
  }


  def calculateShipment(cartItems: List[CartItem]): ShipmentTotalOption = {
    val cartShipmentOptions = cartItems.foldLeft(Nil: List[ShipmentItem]) {
      (sum, cartItem) =>
        val item = persistence.Item.getItem(cartItem.itemId)
        val itemShipmentOption = generateItemShipmentInfo(item)
        itemShipmentOption :: sum
    }

    val nonEmptyShipmentOptions: List[ShipmentItemInfo] = for (si <- cartShipmentOptions if (si.isInstanceOf[ShipmentItemInfo])) yield si.asInstanceOf[ShipmentItemInfo]

    val totalWeight = nonEmptyShipmentOptions.map(x => x.weight).sum
    val envelopeFit = {
      if (cartShipmentOptions.size > 1) false
      else if (nonEmptyShipmentOptions(0).envelopeFit) true
      else false
    }

    val smallBoxFit = {
      if (nonEmptyShipmentOptions.map(x => x.smallBoxFit).sum > 100) false
      else true
    }

    val mediumBoxFit = {
      if (nonEmptyShipmentOptions.map(x => x.mediumBoxFit).sum > 100) false
      else true
    }

    val largeBoxFit = {
      if (nonEmptyShipmentOptions.map(x => x.largeBoxFit).sum > 100) false
      else true
    }

    val shipmentTotalOption = ShipmentTotalOption(totalWeight, envelopeFit, smallBoxFit, mediumBoxFit, largeBoxFit)

    println(cartShipmentOptions)

    println(shipmentTotalOption)

    shipmentTotalOption
  }


}
