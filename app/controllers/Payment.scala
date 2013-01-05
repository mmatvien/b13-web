package controllers

import play.api.mvc._
import util._

/**
 * User: max
 * Date: 1/3/13
 * Time: 9:01 PM
 */
object Payment extends Controller with SessionHelper {


  def payment = Action {
    implicit request =>
      val tokenId = request.body.asFormUrlEncoded.get("stripeToken").head

      println("tokenId = " + tokenId)

      val charge = Charge.create(Map("amount" -> 100, "currency" -> "usd", "card" -> tokenId))
      val retrievedToken = Token.retrieve(tokenId)


      println(charge)

      Ok(views.html.thankyou("123"))

  }

}
