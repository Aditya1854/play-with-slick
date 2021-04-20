package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject


/* this method verify that the requested url is valid for all the request
* */

class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def preflight(all: String): Action[AnyContent] = Action {_ =>
    Ok("").withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "*",
      "Access-Control-Allow-Headers" ->
        "*")
  }

}
