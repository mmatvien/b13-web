package persistence

/**
 * User: max
 * Date: 1/5/13
 * Time: 7:35 PM
 */

import com.mongodb.casbah.Imports._
import com.novus.salat.dao._
import java.util.Date


case class Buyer(
                  firstName: String,
                  lastName: String,
                  email: String,
                  phone: String,
                  country: String,
                  region: String,
                  city: String,
                  street: String,
                  building: String,
                  apt: String,
                  zip: String
                  )


case class Order(
                  date: Date,
                  orderState: Int,
                  sessionId: String,
                  chargeId:String,
                  orderId: String,
                  buyer: Buyer = Buyer("","","","","","","","","","","")
                  )

object Order extends ModelCompanion[Order, ObjectId] {

  val collection = MongoConnection()("b13_web")("order")
  val dao = new SalatDAO[Order, ObjectId](collection = collection) {}

}
