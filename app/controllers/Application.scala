package controllers

import persistence._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import persistence.CartItem
import persistence.CartItemVariation
import scala.Some


object Application extends Controller with SessionHelper {


  def index = Action {
    implicit request =>
      Ok(views.html.index("Your new application is ready.")).withSession("uuid" -> sessionN(request))
  }


  def collection(collection: String, page: Int) = Action {
    implicit request =>

      val filter = if (request.queryString.contains("filter")) request.queryString("filter").head else ""
      val size = if (request.queryString.contains("size")) request.queryString("size").head else ""
      val cat = if (request.queryString.contains("cat")) request.queryString("cat").head else ""

      val items = Item.findItems(collection, cat, filter, size, page)
      val pagerSize = Item.findPagerSize(collection, cat, filter, size)

      Ok(views.html.collection(collection, items, page, pagerSize)).withSession("uuid" -> sessionN(request))
  }


  def item(collection: String, itemId: String) = Action {
    implicit request =>
      Ok(views.html.item(Item.getItem(itemId))).withSession("uuid" -> sessionN(request))
  }


  /**
   * cart Form definition.
   */

  case class CartItemX(collection: String, itemId: String, price: String, variations: List[CartItemVariationX])

  case class CartItemVariationX(name: String, value: String)

  val cartForm: Form[CartItemX] = Form(
    mapping(
      "collection" -> nonEmptyText,
      "itemId" -> nonEmptyText,
      "price" -> nonEmptyText,
      "variations" -> list(mapping(
        "name" -> text,
        "value" -> text
      )(CartItemVariationX.apply)(CartItemVariationX.unapply))
    )(CartItemX.apply)(CartItemX.unapply)
  )

  def add = Action {
    implicit request =>
      cartForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (cartItemX: CartItemX) => {
          Cart.findSessionCart(sessionInfo.sessionId) match {
            case Some(currentCart: Cart) =>
              Cart.addItemToCart(currentCart, CartItem(cartItemX.collection, cartItemX.itemId, 1, cartItemX.price.toDouble, cartItemX.variations.map(v => CartItemVariation(v.name, v.value))))
            case None => Cart.createNewCart(sessionInfo.sessionId, CartItem(cartItemX.collection, cartItemX.itemId, 1, cartItemX.price.toDouble, cartItemX.variations.map(v => CartItemVariation(v.name, v.value))))
          }
        }
      }
      )
      Redirect("/cart").flashing("success" -> "cart item added")
  }


  def currentCartItems(implicit sessionInfo: SessionInfo): List[CartItem] = Cart.findSessionCart(sessionInfo.sessionId) match {
    case Some(cart) => cart.cartItems
    case None => List()
  }


  def remove(cartItemHash: String) = Action {
    implicit request =>

      Cart.removeItem(sessionInfo.sessionId, cartItemHash)

      Redirect("/cart").flashing("success" -> "cart item removed")
  }

  case class CartItemZ(itemId: String, quantity: String, variations: List[CartItemVariationX])

  case class CartItemList(items: List[CartItemZ])

  val cartUpdateForm: Form[CartItemList] = Form(
    mapping(
      "items" -> list(mapping(
        "itemId" -> text,
        "quantity" -> text,
        "variations" -> list(mapping(
          "name" -> text,
          "value" -> text
        )(CartItemVariationX.apply)(CartItemVariationX.unapply))
      )(CartItemZ.apply)(CartItemZ.unapply))
    )(CartItemList.apply)(CartItemList.unapply)
  )


  def update = Action {
    implicit request =>
      val action = request.body.asFormUrlEncoded.get("update").head

      cartUpdateForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (itemList: CartItemList) => {
          for (cartItem: CartItemZ <- itemList.items) {
            if (cartItem.quantity.toInt > 0) Cart.updateCartItem(sessionInfo.sessionId, cartItem.itemId, cartItem.quantity.toInt, cartItem.variations.map(v => CartItemVariation(v.name, v.value)))
          }
        }
      }
      )

      if (action == "оформить") Ok(views.html.payment(currentCartItems)).flashing("success" -> "cart item updated")
      else Ok(views.html.cart(currentCartItems)).flashing("success" -> "cart item updated")
  }

  def cart = Action {
    implicit request =>
      Ok(views.html.cart(currentCartItems))
  }

  val subscribeForm: Form[Subscription] = Form(
    mapping(
      "email" -> text
    )(Subscription.apply)(Subscription.unapply))


  def subscribe = Action {
    implicit request =>
      subscribeForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (subscription: Subscription) => {
          println(subscription.email)
          Ok.flashing("success" -> "спасибо за подписку")
        }
        Ok.flashing("success" -> "спасибо за подписку")
      }
      )
  }
}

