package controllers

import play.api.mvc._
import util._
import persistence._
import play.api.data.Form
import play.api.data.Forms._
import java.util.Date
import services.EmailService
import concurrent.duration._
import persistence.CartItem
import scala.Some
import services.SmtpConfig
import services.EmailMessage
import persistence.Buyer
import xml.Elem
import math.BigDecimal.RoundingMode._
import persistence.CartItem
import scala.Some
import services.SmtpConfig
import services.EmailMessage
import persistence.Buyer


/**
 * User: max
 * Date: 1/3/13
 * Time: 9:01 PM
 */
object Payment extends Controller with SessionHelper {

  case class PaymentInfo(
                          total: String,
                          subtotal: String,
                          shipping: String,
                          insurance: String,
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
      "subtotal" -> nonEmptyText,
      "shipping" -> nonEmptyText,
      "insurance" -> nonEmptyText,
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
        sendEmails(cart, payment, orderId)
        orderId
      }
      case None => "ошибка, корзина пуста"
    }

  }

  def checkCartItems(sessionId: String): List[CartItem] = {
    val sessionCart: Option[Cart] = Cart.findSessionCart(sessionId)
    sessionCart match {
      case Some(cart) => services.EbayService.checkCartWithVendor(cart)
      case None => Nil
    }
  }

  def payment = Action {
    implicit request =>

      paymentForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (payment: PaymentInfo) => {
          val failedCartItems = checkCartItems(sessionInfo.sessionId)
          if (failedCartItems.isEmpty) {
            val grandTotalDollars = (payment.total.toDouble / Calculator.KURS_DOLLARA).setScale(2, HALF_UP)


            val cents =  BigDecimal((grandTotalDollars*100).toString).setScale(0, HALF_UP)
            println(s"charging for amount: $payment.total = $cents")

            val charge: Charge = Charge.create(Map("amount" -> cents, "currency" -> "usd",
              "card" -> payment.stripeToken))
            val token: Token = Token.retrieve(payment.stripeToken)

            val orderId = createOrder(sessionInfo.sessionId, token.id, charge.id, payment)
            Ok(views.html.thankyou(orderId)).withNewSession
          } else {
            println(s" FAILED ITEMS : -------------------- $failedCartItems")
            var message = "<b>следующие товары отсутсвуют на складе</b>:<br><br>"
            failedCartItems.foreach {
              cit =>
                val item = Item.getItem(cit.itemId)
                item match {
                  case Some(it) => message += " <i>-- " + it.title + "</i><br>"
                  case None => ""
                }
                Cart.removeItem(sessionInfo.sessionId, cit.toHash)
            }
            Ok(views.html.payment(payment.subtotal, payment.shipping, payment.insurance,
              payment.total.toDouble)(message))
          }
        }
      }
      )
  }

  def sendEmails(cart: Cart, payment: PaymentInfo, orderId: String) {


    def itemRow(item: CartItem): Elem = {
      val row =
        <tr>
          <td style="width:200px;">
            {persistence.Item.getItem(item.itemId).get.title}
          </td>
           <td style="width:200px;">
            {item.variations.map(vari => vari.variationName + ":" + vari.variationValue)}
          </td>
          <td style="width:70px;">
            {item.price}
          </td>
          <td style="width:20px;">
            {item.quantity}
          </td>
        </tr>
      row
    }


    val table =
      <table class="table table-bordered">
        {cart.cartItems.map(item => itemRow(item))}
      </table>



    val messageHtml =
      s"""
        |
        |Спасибо за покупку в магазине BRAND13<br><br>
        |Номер вашего заказа <b>$orderId</b><br><br>
        |
        |Содержимое заказа:<br><br>
        |<hr>
        |
        |$table
        |<hr>
        |<br>
        |товары на сумму: ${payment.subtotal}<br>
        |доставка: ${payment.shipping}<br>
        |страховка: ${payment.insurance}<br><br>
        |
        |Итого к оплате:<b> ${payment.total}</b> <br> <br> <br>
        |
        |По факту отправки заказа из США вам будет отправлено электронное письмо с номером отслеживания.<br>
        |
        |По всем интересующим вас вопросам обращайтесь к нам через наш сайт, раздел Вопросы (не забудьте указать
        номер заказа).
        |
        |<br><br>
        |
        |Мы ценим сотрудничество с вами и надеемся на его продолжение!
        |
        | <br><br>
        |
        |<i>С уважением, администрация BRAND13</i>
        |<a href="http://brand13.com/"> <img src="http://brand13.com/assets/images/logo-265.png" style="width: 150px;
        "/> </a>
        |<br>
      """.stripMargin


    val config = SmtpConfig(
      tls = true,
      ssl = true,
      465,
      "smtp.gmail.com",
      "maxirullc@gmail.com",
      "azoezihiukncjnfi"
    )

    val email = EmailMessage(
      "brand 13 - Спасибо за покупку",
      payment.email,
      "sales@brand13.com",
      "text",
      messageHtml,
      config,
      10 seconds,
      3
    )

    EmailService.send(email)


    val config2 = SmtpConfig(
      tls = true,
      ssl = true,
      465,
      "smtp.gmail.com",
      "maxirullc@gmail.com",
      "azoezihiukncjnfi"
    )

    val email2 = EmailMessage(
      "brand 13 - Order placed",
      "maxirullc@gmail.com",
      "sales@brand13.com",
      "text",
      messageHtml,
      config,
      10 seconds,
      3
    )

    EmailService.send(email)

  }
}
