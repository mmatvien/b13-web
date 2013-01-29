/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

package controllers

import play.api.data.Form
import play.api.data.Forms._
import views.html
import play.api.mvc._
import persistence.Account

/**
 * User: max
 * Date: 1/29/13
 * Time: 10:37 AM
 */
object Auth extends Controller {

  val loginForm = Form {
    mapping("email" -> email, "password" -> text)(Account.authenticate)(_.map(u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  def login = Action {
    implicit request =>
      Ok(html.login(loginForm))
  }

  def logout = Action {
    implicit request =>
      Redirect(routes.Auth.login).withNewSession.flashing(
        "success" -> "You've been logged out")
  }

  def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.login(formWithErrors)),
        user => Redirect(routes.Admin.index).withSession("email" -> user.get.email))
  }
}

/**
 * Provide security features
 */
trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Auth.login)

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) {
    user =>
      Action(request => f(user)(request))
  }

}
