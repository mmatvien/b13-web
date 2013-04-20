/*
 * Copyright (c) 2012.
 * All Right Reserved, http://www.maxiru.com/
 */

package persistence

/**
 * User: max
 * Date: 12/19/12
 * Time: 2:43 PM
 */

import com.mongodb.casbah.Imports._
import com.novus.salat.dao._
import java.util.Date

case class CartItem(
                     collection: String,
                     itemId: String,
                     quantity: Int,
                     price: BigDecimal,
                     variations: List[CartItemVariation]
                     ) {
  def toHash: String = itemId + variations.toString().hashCode
}

case class CartItemVariation(variationName: String, variationValue: String)

case class Cart(
                 date: Date,
                 sessionId: String,
                 cartState: Int,
                 cartItems: List[CartItem] = List[CartItem]
                 )

object Cart extends ModelCompanion[Cart, ObjectId] {


  val collection = MongoConnection()("b13_web")("cart")
  val dao = new SalatDAO[Cart, ObjectId](collection = collection) {}

  // -- Queries

  def findSessionCart(sessionId: String): Option[Cart] = {
    println("++++++++++++++++++++++ fetching cart for session " + sessionId)
    dao.find(MongoDBObject("sessionId" -> sessionId, "cartState" -> 0)).toList.headOption
  }

  def findOrderCart(sessionId: String): Cart =
    dao.find(MongoDBObject("sessionId" -> sessionId, "cartState" -> 1)).toList.headOption.get

  def updateCartItem(sessionId: String, itemId: String, quantityA: Int, variations: List[CartItemVariation]) {
    findSessionCart(sessionId: String) match {
      case Some(cart) => {
        findCartItem(cart, itemId, variations) match {
          case Some(cartItemX) => {
            val updatedItem = cartItemX.copy(quantity = quantityA)
            val restOfItems = cart.cartItems.filter(_.itemId != itemId) ::: cart.cartItems.filter(x => (x.itemId == itemId && x.variations != variations))
            val newList = updatedItem :: restOfItems
            dao.update(q = MongoDBObject("sessionId" -> cart.sessionId),
              t = cart.copy(cartItems = newList.reverse),
              upsert = false, multi = false, wc = Cart.dao.collection.writeConcern)
          }
          case None =>
        }

      }
      case None => // do nothing
    }

  }

  def createNewCart(sessionId: String, cartItem: CartItem) {
    dao.insert(Cart(new Date, sessionId, 0, List(cartItem)))
  }


  def findCartItem(cart: Cart, itemId: String, variations: List[CartItemVariation]): Option[CartItem] = {
    cart.cartItems.filter(_.itemId == itemId).filter(_.variations == variations).headOption
  }

  def updateCartState(cart: Cart, state: Int) {
    dao.update(q = MongoDBObject("sessionId" -> cart.sessionId, "cartState" -> 0),
      t = cart.copy(cartState = state),
      upsert = false, multi = false, wc = Cart.dao.collection.writeConcern)
  }

  /**
   * @param cart existing cart
   * @param cartItem card item to add
   */
  def addItemToCart(cart: Cart, cartItem: CartItem) {
    val itemList = findCartItem(cart, cartItem.itemId, cartItem.variations) match {
      case Some(cartItemX) => cartItemX.copy(quantity = cartItemX.quantity + 1) :: cart.cartItems.filter(_.itemId != cartItem.itemId)
      case None => cartItem :: cart.cartItems
    }

    dao.update(q = MongoDBObject("sessionId" -> cart.sessionId),
      t = cart.copy(cartItems = itemList),
      upsert = false, multi = false, wc = Cart.dao.collection.writeConcern)
  }


  def removeItem(sessionId: String, itemToRemoveHash: String) {
    findSessionCart(sessionId) match {
      case Some(cart) => {
        if (cart.cartItems.size > 1) {
          val newList = cart.cartItems.filter(item => item.toHash != itemToRemoveHash)
          dao.update(q = MongoDBObject("sessionId" -> cart.sessionId),
            t = cart.copy(cartItems = newList),
            upsert = false, multi = false, wc = Cart.dao.collection.writeConcern)
        } else {
          dao.remove(cart)
        }
      }
      case None => // do nothing
    }
  }
}