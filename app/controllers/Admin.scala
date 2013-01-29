package controllers


import play.api.mvc._
import persistence.{Ops, Item, Order}

object Admin extends Controller with Secured {

  def index = orders

  def orders = IsAuthenticated {
    email => {
      implicit request =>
        Ok(views.html.admin_orders(Order.findAll().toList, email, "orders"))
    }
  }


  def ordered(collection: String, cartItemId: String) = IsAuthenticated {
    email => {
      implicit request =>

        println("marking as ordered : " + cartItemId)
        Item.changeState(cartItemId, 1)

        Ok(views.html.admin_orders(Order.findAll().toList, email, "orders"))
    }
  }


  def received(collection: String, cartItemId: String) = IsAuthenticated {
    email => {
      implicit request =>

        println("marking as received : " + cartItemId)
        Item.changeState(cartItemId, 2)

        Ok(views.html.admin_orders(Order.findAll().toList, email, "orders"))
    }
  }


  def inventory = IsAuthenticated {
    email => {
      implicit request =>
        Ok(views.html.admin_inventory(Ops.getLatest, email, "inventory"))
    }
  }

}

