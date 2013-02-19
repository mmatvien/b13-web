/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

package util

/**
 * User: max
 * Date: 2/18/13
 * Time: 3:51 PM
 */
object ShippingRef {

  case class ShippingPrice(gramsFrom: BigDecimal, gramsTo: BigDecimal, price: BigDecimal)

  case class PriorityInsurance(ins: BigDecimal, price: BigDecimal)


  val firstClass = List(
    ShippingPrice(0, 28.3495, 6.55),
    ShippingPrice(28.3495, 57, 6.55),
    ShippingPrice(57, 85.0485, 9.45),
    ShippingPrice(85.0495, 113.398, 9.45),
    ShippingPrice(113.398, 142, 12.75),
    ShippingPrice(142, 170.097, 12.75),
    ShippingPrice(170.097, 198.4465, 12.75),
    ShippingPrice(198.4465, 227, 12.75),
    ShippingPrice(227, 340.194, 14.90),
    ShippingPrice(340.194, 453.592, 16.75),
    ShippingPrice(453.592, 567, 18.60),
    ShippingPrice(567, 680.388, 20.45),
    ShippingPrice(680.388, 794, 22.30),
    ShippingPrice(794, 907.184, 24.15),
    ShippingPrice(907.184, 1021, 26.00),
    ShippingPrice(1021, 1134, 27.85),
    ShippingPrice(1134, 1247.378, 29.70),
    ShippingPrice(1247.378, 1361, 31.55),
    ShippingPrice(1361, 1474.174, 33.40),
    ShippingPrice(1474.174, 1588, 35.25),
    ShippingPrice(1588, 1701, 37.10),
    ShippingPrice(1701, 1814.369, 38.95)
  )

  val priorityMail = List(
    ShippingPrice(0,453.592,41),
    ShippingPrice(453.592,907.184,45.45),
    ShippingPrice(907.184,1361,49.90),
    ShippingPrice(1361,1814.369,54.35),
    ShippingPrice(1814.369,2267.96,58.80),
    ShippingPrice(2267.96,2721.552,62.85),
    ShippingPrice(2721.552,3175.144,66.90),
    ShippingPrice(3175.144,3628.736,70.95),
    ShippingPrice(3628.736,4082.328,75.00),
    ShippingPrice(4082.328,4535.92,79.05),
    ShippingPrice(4535.92,4989.512,83.10),
    ShippingPrice(4989.512,5443.104,87.15),
    ShippingPrice(5443.104,5896.696,91.20),
    ShippingPrice(5896.696,6350.288,95.25),
    ShippingPrice(6350.288,6803.88,99.30),
    ShippingPrice(6803.88,7257.472,103.35),
    ShippingPrice(7257.472,7711.064,107.40),
    ShippingPrice(7711.064,8164.656,111.45),
    ShippingPrice(8164.656,8618.248,115.50),
    ShippingPrice(8618.248,9071.84,119.55),
    ShippingPrice(9071.84,9525.432,123.60),
    ShippingPrice(9525.432,9979.024,127.65),
    ShippingPrice(9979.024,10432.616,131.70),
    ShippingPrice(10432.616,10886.208,135.75),
    ShippingPrice(10886.208,11339.8,139.80),
    ShippingPrice(11339.8,11793.392,143.85),
    ShippingPrice(11793.392,12246.984,147.90),
    ShippingPrice(12246.984,12700.576,151.95),
    ShippingPrice(12700.576,13154.168,156.00),
    ShippingPrice(13154.168,13607.76,160.05),
    ShippingPrice(13607.76,14061.352,164.10),
    ShippingPrice(14061.352,14514.944,168.15),
    ShippingPrice(14514.944,14968.563,172.20),
    ShippingPrice(14968.563,15422.128,176.25),
    ShippingPrice(15422.128,15875.72,180.30),
    ShippingPrice(15875.72,16329.312,184.35),
    ShippingPrice(16329.312,16782.904,188.40),
    ShippingPrice(16782.904,17236.496,192.45),
    ShippingPrice(17236.496,17690.088,196.50),
    ShippingPrice(17690.088,18143.68,200.55),
    ShippingPrice(18143.68,18597.272,204.60),
    ShippingPrice(18597.272,19050.864,208.65),
    ShippingPrice(19050.864,19504.456,212.70),
    ShippingPrice(19504.456,19958.049,216.75)
  )

  val priorityInsurance = List(
    PriorityInsurance(50.00, 2.50),
    PriorityInsurance(100.00, 3.40),
    PriorityInsurance(200.00, 4.40),
    PriorityInsurance(300.00, 5.40),
    PriorityInsurance(400.00, 6.40),
    PriorityInsurance(500.00, 7.50),
    PriorityInsurance(600.00, 9.00),
    PriorityInsurance(700.00, 10.50),
    PriorityInsurance(800.00, 12.00),
    PriorityInsurance(900.00, 13.50),
    PriorityInsurance(1000.00, 15.00),
    PriorityInsurance(1100.00, 16.50),
    PriorityInsurance(1200.00, 18.00),
    PriorityInsurance(1300.00, 19.50),
    PriorityInsurance(1400.00, 21.00),
    PriorityInsurance(1500.00, 22.50),
    PriorityInsurance(1600.00, 24.00),
    PriorityInsurance(1700.00, 25.50),
    PriorityInsurance(1800.00, 27.00),
    PriorityInsurance(1900.00, 28.50),
    PriorityInsurance(2000.00, 30.00),
    PriorityInsurance(2100.00, 52.50),
    PriorityInsurance(2200.00, 55.00),
    PriorityInsurance(2300.00, 57.50),
    PriorityInsurance(2400.00, 60.00),
    PriorityInsurance(2500.00, 62.50),
    PriorityInsurance(2600.00, 65.00),
    PriorityInsurance(2700.00, 67.50),
    PriorityInsurance(2800.00, 70.00),
    PriorityInsurance(2900.00, 72.50),
    PriorityInsurance(3000.00, 75.00),
    PriorityInsurance(3100.00, 77.50),
    PriorityInsurance(3200.00, 80.00),
    PriorityInsurance(3300.00, 82.50),
    PriorityInsurance(3400.00, 85.00),
    PriorityInsurance(3500.00, 87.50),
    PriorityInsurance(3600.00, 90.00),
    PriorityInsurance(3700.00, 92.50),
    PriorityInsurance(3800.00, 95.00),
    PriorityInsurance(3900.00, 97.50),
    PriorityInsurance(4000.00, 100.00),
    PriorityInsurance(4100.00, 102.50),
    PriorityInsurance(4200.00, 105.00),
    PriorityInsurance(4300.00, 107.50),
    PriorityInsurance(4400.00, 110.00),
    PriorityInsurance(4500.00, 112.50),
    PriorityInsurance(4600.00, 115.00),
    PriorityInsurance(4700.00, 117.50),
    PriorityInsurance(4800.00, 120.00),
    PriorityInsurance(4900.00, 122.50),
    PriorityInsurance(5000.00, 125.00)
  )

}
