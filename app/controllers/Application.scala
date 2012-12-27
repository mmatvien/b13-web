package controllers

import persistence.{CartItemVariation, Cart, Item, CartItem}

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.Some


object Application extends Controller with SessionHelper {


  def index = Action {
    implicit request =>
      Ok(views.html.index("Your new application is ready."))
  }


  def guess = Action {
    implicit request =>

      val items = Item.findItems()
           println(items.size)
      Ok(views.html.guess(items))
  }


  def item(itemId: String) = Action {
    implicit request =>
      Ok(views.html.item(Item.getItem(itemId)))
  }


  /**
   * cart Form definition.
   */

  case class CartItemX(itemId: String, price: String, variations: List[CartItemVariationX])

  case class CartItemVariationX(name: String, value: String)

  val cartForm: Form[CartItemX] = Form(
    mapping(
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
              Cart.addItemToCart(currentCart, CartItem(cartItemX.itemId, 1, cartItemX.price.toDouble, cartItemX.variations.map(v => CartItemVariation(v.name, v.value))))
            case None => Cart.createNewCart(sessionInfo.sessionId, CartItem(cartItemX.itemId, 1, cartItemX.price.toDouble, cartItemX.variations.map(v => CartItemVariation(v.name, v.value))))
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

      println(request.queryString)
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
      cartUpdateForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (itemList: CartItemList) => {
          for (cartItem: CartItemZ <- itemList.items) {
            if (cartItem.quantity.toInt > 0) Cart.updateCartItem(sessionInfo.sessionId, cartItem.itemId, cartItem.quantity.toInt, cartItem.variations.map(v => CartItemVariation(v.name, v.value)))
          }
        }
      }
      )

      Ok(views.html.cart(currentCartItems)).flashing("success" -> "cart item updated")
  }

  def cart = Action {
    implicit request =>
      Ok(views.html.cart(currentCartItems))
  }

}

