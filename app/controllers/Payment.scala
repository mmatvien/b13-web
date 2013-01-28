package controllers

import play.api.mvc._
import util._
import persistence.{Buyer, Order, Cart}
import play.api.data.Form
import play.api.data.Forms._
import java.util.Date
import scala.Some


/**
 * User: max
 * Date: 1/3/13
 * Time: 9:01 PM
 */
object Payment extends Controller with SessionHelper {

  case class PaymentInfo(
                          total: String,
                          stripeToken: String,
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


  val paymentForm: Form[PaymentInfo] = Form(
    mapping(
      "total" -> nonEmptyText,
      "stripeToken" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> nonEmptyText,
      "phone" -> nonEmptyText,
      "country" -> nonEmptyText,
      "region" -> nonEmptyText,
      "city" -> nonEmptyText,
      "street" -> nonEmptyText,
      "building" -> nonEmptyText,
      "apt" -> nonEmptyText,
      "zip" -> nonEmptyText
    )(PaymentInfo.apply)(PaymentInfo.unapply)
  )

  def createOrder(sessionId: String, stripeToken: String, chargeId: String, payment: PaymentInfo): String = {

    val sessionCart: Option[Cart] = Cart.findSessionCart(sessionId)
    sessionCart match {
      case Some(cart) => {
        val orderId = "b13_" + System.currentTimeMillis
        Order.save(
          Order(new Date,
            0,
            sessionId,
            chargeId: String,
            orderId: String,
            Buyer(
              payment.firstName,
              payment.lastName,
              payment.email,
              payment.phone,
              payment.country,
              payment.region,
              payment.city,
              payment.street,
              payment.building,
              payment.apt,
              payment.zip
            )
          )
        )

        Cart.updateCartState(cart, 1)
        orderId
      }
      case None => "ошибка, корзина пуста"
    }

  }

  def payment = Action {
    implicit request =>

      paymentForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (payment: PaymentInfo) => {
          //TODO: implement ebay check on checkout
          //val checkItemsOnEbay()

          println("charging for amount: " + payment)
          val charge: Charge = Charge.create(Map("amount" -> 100, "currency" -> "usd", "card" -> payment.stripeToken))
          val token: Token = Token.retrieve(payment.stripeToken)

          val orderId = createOrder(sessionInfo.sessionId, token.id, charge.id, payment)
          Ok(views.html.thankyou(orderId)).withNewSession
        }
        //Ok(views.html.thankyou("error"))
      }
      )

    // Ok(views.html.thankyou("123"))


  }

}
