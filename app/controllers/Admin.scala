package controllers


import play.api.mvc._
import persistence.{Item, Order}

object Admin extends Controller {

  def index = Action {
    implicit request =>
      Ok(views.html.admin(Order.findAll().toList))
  }


  def ordered(collection: String, cartItemId: String) = Action {
    implicit request =>

      println("marking as ordered : " + cartItemId)
      Item.changeState(cartItemId, 1)

      Ok(views.html.admin(Order.findAll().toList))
  }


  def received(collection: String, cartItemId: String) = Action {
    implicit request =>

      println("marking as received : " + cartItemId)
      Item.changeState(cartItemId, 2)

      Ok(views.html.admin(Order.findAll().toList))
  }


}

