package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject

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
