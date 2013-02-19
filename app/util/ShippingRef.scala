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
    ShippingPrice(0, 28, 6.55),
    ShippingPrice(28, 57, 6.55),
    ShippingPrice(57, 85, 9.45),
    ShippingPrice(85, 113, 9.45),
    ShippingPrice(113, 142, 12.75),
    ShippingPrice(142, 170, 12.75),
    ShippingPrice(170, 198, 12.75),
    ShippingPrice(198, 227, 12.75),
    ShippingPrice(227, 340, 14.90),
    ShippingPrice(340, 454, 16.75),
    ShippingPrice(454, 567, 18.60),
    ShippingPrice(567, 680, 20.45),
    ShippingPrice(680, 794, 22.30),
    ShippingPrice(794, 907, 24.15),
    ShippingPrice(907, 1021, 26.00),
    ShippingPrice(907, 1134, 27.85),
    ShippingPrice(1134, 1247, 29.70),
    ShippingPrice(1247, 1361, 31.55),
    ShippingPrice(1361, 1474, 33.40),
    ShippingPrice(1474, 1588, 35.25),
    ShippingPrice(1588, 1701, 37.10),
    ShippingPrice(1701, 1814.369, 38.95)
  )

  val priorityMail = List(
    ShippingPrice(0, 454, 41),
    ShippingPrice(454, 907, 45.45),
    ShippingPrice(907, 1361, 49.90),
    ShippingPrice(1361, 1814, 54.35),
    ShippingPrice(1814, 2268, 58.80),
    ShippingPrice(2268, 2722, 62.85),
    ShippingPrice(2722, 3175, 66.90),
    ShippingPrice(3175, 3629, 70.95),
    ShippingPrice(3629, 4082, 75.00),
    ShippingPrice(4082, 4536, 79.05),
    ShippingPrice(4536, 4990, 83.10),
    ShippingPrice(4990, 5443, 87.15),
    ShippingPrice(5443, 5897, 91.20),
    ShippingPrice(5897, 6350, 95.25),
    ShippingPrice(6350, 6804, 99.30),
    ShippingPrice(6804, 7257, 103.35),
    ShippingPrice(7257, 7711, 107.40),
    ShippingPrice(7711, 8165, 111.45),
    ShippingPrice(8165, 8618, 115.50),
    ShippingPrice(8618, 9072, 119.55),
    ShippingPrice(9072, 9525, 123.60),
    ShippingPrice(9525, 9979, 127.65),
    ShippingPrice(9979, 10433, 131.70),
    ShippingPrice(10433, 10886, 135.75),
    ShippingPrice(10886, 11340, 139.80),
    ShippingPrice(11340, 11793, 143.85),
    ShippingPrice(11793, 12247, 147.90),
    ShippingPrice(12247, 12701, 151.95),
    ShippingPrice(12701, 13154, 156.00),
    ShippingPrice(13154, 13608, 160.05),
    ShippingPrice(13608, 14061, 164.10),
    ShippingPrice(14061, 14515, 168.15),
    ShippingPrice(14515, 14969, 172.20),
    ShippingPrice(14969, 15422, 176.25),
    ShippingPrice(15422, 15876, 180.30),
    ShippingPrice(15876, 16329, 184.35),
    ShippingPrice(16329, 16783, 188.40),
    ShippingPrice(16783, 17237, 192.45),
    ShippingPrice(17237, 17690, 196.50),
    ShippingPrice(17690, 18144, 200.55),
    ShippingPrice(18144, 18597, 204.60),
    ShippingPrice(18597, 19051, 208.65),
    ShippingPrice(19051, 19504, 212.70),
    ShippingPrice(19504, 19958.049, 216.75)
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
