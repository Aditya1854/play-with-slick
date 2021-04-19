package controllers
import com.google.inject.Inject
import models.{User,UserData,UserName}
import play.api.libs.json.{JsError,JsValue, Json}
import play.api.mvc._
import repository.UserRepository
import utils.JsonFormat.{userData,userVal}

import scala.concurrent.{ExecutionContext, Future}
import utilities.JwtUtility

class UserController @Inject()(cc: ControllerComponents,auth : JwtUtility,userRepository: UserRepository)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {
  def create: Action[JsValue] =
    Action.async(parse.json) { request =>
      request.body.validate[User].fold(error => Future.successful(BadRequest(JsError.toJson(error))), {user =>
        userRepository.create(user).map { isCreated =>
          Ok(Json.toJson(isCreated)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

  def getById: Action[JsValue] = {
    Action.async(parse.json) { request =>
      request.body.validate[UserData].fold(error => {Future.successful(BadRequest(JsError.toJson(error)))}, { user =>
        userRepository.getById(user.email, user.password).map {
          case Some(user) =>
            Ok(Json.toJson(Map("token" -> auth.encodeToken(UserName(user.firstName))))).withHeaders("Access-Control-Allow-Origin" -> "*")
          case None =>
            BadRequest("Email does not exist").withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }
  }


}
