package util

/**
 * User: max
 * Date: 12/21/12
 * Time: 12:08 PM
 */

import scala.BigDecimal.RoundingMode.HALF_UP
import persistence.{Item, CartItem}
import util.ShippingRef.PriorityInsurance

object Calculator {
  val KURS_DOLLARA: BigDecimal = 31.5


  def calculateFinalPrice(original: BigDecimal): BigDecimal = {
    val commissionShippingToB13 = 3
    val commissionPercent = calculateCommissionPercentage(original)
    val commissionDollars = (original / 100 * commissionPercent)
    val salesTax = original / 100 * 7
    val result: BigDecimal = KURS_DOLLARA * (commissionDollars + commissionShippingToB13 + original + salesTax)
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
                                 largeBoxFit: Boolean,
                                 firstClassAvailable:Boolean ) {
    override def toString = s"total weight = $totalWeight envelope fit = $envelopeFit : small box fit = $smallBoxFit : medium box fit = $mediumBoxFit : large box fit = $largeBoxFit"
  }

  trait ShipmentItem

  case class ShipmentItemInfo(weight: BigDecimal,
                              envelopeFit: Boolean,
                              smallBoxFit: Int,
                              mediumBoxFit: Int,
                              largeBoxFit: Int,
                              firstClassAvailable:Boolean) extends ShipmentItem

  case object ShipmentItemEmpty extends ShipmentItem


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
      val firstClassAvailable = !category.contains("Wristwatches")

      ShipmentItemInfo(itemWeight, envelope == 0, smallBoxPercentage.toInt, mediumBoxPercentage.toInt, largeBoxPercentage.toInt, firstClassAvailable)
    }
  }


  def calculateShipment(cartItems: List[CartItem]): ShipmentTotalOption = {

    val cartShipmentOptions = cartItems.foldLeft(Nil: List[ShipmentItem]) {
      (sum, cartItem) =>
       persistence.Item.getItem(cartItem.itemId) match {
         case Some(item) =>  {
           val itemShipmentOption = generateItemShipmentInfo(item)
           val all = for (x <- 1 to cartItem.quantity) yield itemShipmentOption
           all.toList ::: sum
         }
         case None => sum
       }

    }

    def convertToGrams(oz: String): BigDecimal = {
      val split = oz.split('.')
      val o = BigDecimal(split(0).toInt)
      if (split(1).toString.length < 2)
        ((o * 16 + (("0" + split(1)).toInt * 10))) * 28.3495
      else {
        (o * 16 + BigDecimal(split(1).toInt)) * 28.3495
      }
    }


    val nonEmptyShipmentOptions: List[ShipmentItemInfo] = for (si <- cartShipmentOptions if (si.isInstanceOf[ShipmentItemInfo])) yield si.asInstanceOf[ShipmentItemInfo]

    val totalWeight = nonEmptyShipmentOptions.foldLeft(BigDecimal(0))((c, i) => c + convertToGrams(i.weight.toString()))
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

    ShipmentTotalOption(totalWeight, envelopeFit, smallBoxFit, mediumBoxFit, largeBoxFit, nonEmptyShipmentOptions.filter(x => !x.firstClassAvailable).isEmpty)
  }

  def shippingCostPriorityMail(totalWeight: BigDecimal): Float = {
    println(totalWeight)
    ((1 + util.ShippingRef.priorityMail.filter(x => (x.gramsFrom <= totalWeight && x.gramsTo > totalWeight)).head.price) * KURS_DOLLARA).toFloat
  }

  def shippingCostFirstClassMail(totalWeight: BigDecimal): Float = {
    println(totalWeight)
    ((1 + util.ShippingRef.firstClass.filter(x => (x.gramsFrom <= totalWeight && x.gramsTo > totalWeight)).head.price) * KURS_DOLLARA).toFloat
  }


  def calculateInsurance(cost: BigDecimal): Float = {
    val filtered = util.ShippingRef.priorityInsurance.filter(x => x.ins * KURS_DOLLARA < (cost + 100 * KURS_DOLLARA))

    (filtered.reverse.head.price * KURS_DOLLARA ).toFloat
  }
}
