package controllers


import play.api.mvc._
import persistence._

object Admin extends Controller with Secured {

  def index = orders

  def orders = IsAuthenticated {
    email => {
      implicit request =>
        Ok(views.html.admin_orders(Order.findAll().toList, email, "orders"))
    }
  }


  def ordered(sessionId: String, itemId: String) = IsAuthenticated {
    email => {
      implicit request =>
        println("marking as ordered : " + itemId)
        Cart.changeCartItemState(sessionId, itemId, 1)
        Ok(views.html.admin_orders(Order.findAll().toList, email, "orders"))
    }
  }


  def received(sessionId: String, itemId: String) = IsAuthenticated {
    email => {
      implicit request =>
        println("marking as received : " + itemId)
        Cart.changeCartItemState(sessionId, itemId, 2)
        Ok(views.html.admin_orders(Order.findAll().toList, email, "orders"))
    }
  }


  def inventory = IsAuthenticated {
    email => {
      implicit request =>
        Ok(views.html.admin_inventory(Ops.getLatest, email, "inventory"))
    }
  }


  def questions = IsAuthenticated {
    email => {
      implicit request =>
        Ok(views.html.admin_questions(Question.findAll().toList, email, "questions"))
    }
  }

  def removeSeller(seller: String) = IsAuthenticated {
    email => {
      implicit request =>
        println("removing seller : " + seller)
        persistence.Util.removeSeller(seller)
        Ok(views.html.admin_inventory(Ops.getLatest, email, "inventory"))
    }
  }


  def addSeller() = IsAuthenticated {
    email => {
      implicit request =>
        val map: Map[String, Seq[String]] = request.body.asFormUrlEncoded.getOrElse(Map())
        println("adding seller : " + map.get("sellerName").get.head)
        val sellerName = map.get("sellerName").get.head
        if (!sellerName.isEmpty) {
          Seller.save(Seller(sellerName, persistence.New.toString))
        }
        Ok(views.html.admin_inventory(Ops.getLatest, email, "inventory"))
    }
  }
}

