package controllers

import persistence._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import persistence.CartItem
import persistence.CartItemVariation
import scala.Some
import org.joda.time.DateTime


object Application extends Controller with SessionHelper {


  def index = Action {
    implicit request =>
      Ok(views.html.index("index.")).withSession("uuid" -> sessionN(request))
  }

  def about = Action {
    implicit request =>
      Ok(views.html.about("about.")).withSession("uuid" -> sessionN(request))
  }

  def terms = Action {
    implicit request =>
      Ok(views.html.terms("terms.")).withSession("uuid" -> sessionN(request))
  }


  def collection(section: String, page: Int) = Action {
    implicit request =>

      val filter = if (request.queryString.contains("filter")) request.queryString("filter").head else ""
      val size = if (request.queryString.contains("size")) request.queryString("size").head else ""
      val cat = if (request.queryString.contains("cat")) request.queryString("cat").head else ""
      val br = if (request.queryString.contains("brand")) request.queryString("brand").head else ""
      val brand = br.replaceAll("\\|", "&").replace("(", "\\(").replace(")", "\\)")

      val category = cat.replaceAll("\\|", "&").replace("(", "\\(").replace(")", "\\)").replace("undefined:", "")
      val brands = Item.findBrandsByCategory(section, category, filter)

      if (util.Ref.topCategory.contains(section)) {
        val items = Item.findByCategory(section, category, brand, filter, size, page)
        val pagerSize = Item.findCategoryPagerSize(section, category, brand, filter, size)
        Ok(views.html.collection(section, items, brands, page, pagerSize)).withSession("uuid" -> sessionN(request))
      } else {
        println("seller search " + section)
        val items = Item.findSellerItems(section, cat, filter, size, page)
        val pagerSize = Item.findSellerPagerSize(section, cat, filter, size)
        Ok(views.html.collection(section, items, brands, page, pagerSize)).withSession("uuid" -> sessionN(request))
      }


  }


  def item(collection: String, itemId: String) = Action {
    implicit request =>
      Item.getItem(itemId) match {
        case Some(item) => Ok(views.html.item(item)).withSession("uuid" -> sessionN(request))
        case None => Redirect("/")
      }
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
              Cart.addItemToCart(currentCart, CartItem(cartItemX.collection, cartItemX.itemId, 1,
                cartItemX.price.toDouble, cartItemX.variations.map(v => CartItemVariation(v.name, v.value))))
            case None => Cart.createNewCart(sessionInfo.sessionId, CartItem(cartItemX.collection, cartItemX.itemId,
              1, cartItemX.price.toDouble, cartItemX.variations.map(v => CartItemVariation(v.name, v.value))))
          }
        }
      }
      )
      Redirect("/cart").flashing("success" -> "cart item added")
  }


  def currentCartItems(implicit sessionInfo: SessionInfo): List[CartItem] = Cart.findSessionCart(sessionInfo
    .sessionId) match {
    case Some(cart) => cart.cartItems.filter {
      cartItem => {
        persistence.Item.getItem(cartItem.itemId) match {
          case Some(item) => true
          case None => {
            Cart.removeItem(sessionInfo.sessionId, cartItem.toHash)
            false
          }
        }
      }
    }
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
      val subtotal = request.body.asFormUrlEncoded.get("subtotalPriceInput").head
      val shipping = request.body.asFormUrlEncoded.get("shippingPriceInput").head
      val insurance = request.body.asFormUrlEncoded.get("insurancePriceInput").head
      val total = subtotal.replaceAll("\\s+$", "").toDouble + shipping.replaceAll("\\s+$", "").toDouble + insurance.replaceAll("\\s+$", "").toDouble

      cartUpdateForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (itemList: CartItemList) => {
          for (cartItem: CartItemZ <- itemList.items) {
            if (cartItem.quantity.toInt > 0) Cart.updateCartItem(sessionInfo.sessionId, cartItem.itemId,
              cartItem.quantity.toInt, cartItem.variations.map(v => CartItemVariation(v.name, v.value)))
          }
        }
      }
      )
      if (action == "оформить") Ok(views.html.payment(subtotal,shipping,insurance,total)(""))
      else Ok(views.html.cart(currentCartItems)).flashing("success" -> "cart item updated")
  }

  def cart = Action {
    implicit request =>
      Ok(views.html.cart(currentCartItems))
  }

  val subscribeForm: Form[Subscription] = Form(
    mapping(
      "email" -> text,
      "dateTime" -> optional(jodaDate)
    )(Subscription.apply)(Subscription.unapply))


  def subscribe = Action {
    implicit request =>
      subscribeForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (subscription: Subscription) => {
          Subscription.save(Subscription(subscription.email, Some(DateTime.now)))
          Ok.flashing("success" -> "спасибо за подписку")
        }
        Ok.flashing("success" -> "спасибо за подписку")
      }
      )
  }

  val questionForm: Form[Question] = Form(
    mapping(
      "email" -> text,
      "question" -> text,
      "dateTime" -> optional(jodaDate),
      "state" -> optional(number)
    )(Question.apply)(Question.unapply))


  def question = Action {
    implicit request =>
      questionForm.bindFromRequest.fold(
      errors => BadRequest, {
        case (question: Question) => {
          Question.save(Question(question.email, question.question, Some(DateTime.now), Some(0)))
          Ok.flashing("success" -> "спасибо за вопрос")
        }
        Ok.flashing("success" -> "спасибо за вопрос")
      }
      )
  }
}

