package controllers

import play.api.mvc.Request
import persistence.Cart

/**
 * User: max
 * Date: 12/20/12
 * Time: 9:42 PM
 */

/**
 *
 * @param sessionId uuid randomly generated to identify user session
 * @param itemCount item count for card header
 * @param total     total amount in the cart
 */
case class SessionInfo(
                        sessionId: String,
                        itemCount: Int,
                        total: BigDecimal
                        )


/**
 * Session Helper stores all the information about the user session
 */
trait SessionHelper {

  implicit def sessionInfo[A](implicit request: Request[A]): SessionInfo = {
    request.session.get("uuid") match {
      case Some(uuid) => getCartInfo(uuid)
      case None => {
        val newSession = java.util.UUID.randomUUID().toString
        request.session + ("uuid" -> newSession)
        println(request.session.get("uuid"))
        getCartInfo(newSession)
      }
    }
  }

  def getCartInfo(sessionId: String): SessionInfo = {
    Cart.findSessionCart(sessionId) match {
      case Some(cart) => {
        SessionInfo(
          sessionId,
          cart.cartItems.map(item => item.quantity).sum ,
          cart.cartItems.map(item => item.price * item.quantity).sum
        )
      }
      case None => {
        SessionInfo(
          sessionId,
          0,
          0.0
        )
      }
    }
  }

}