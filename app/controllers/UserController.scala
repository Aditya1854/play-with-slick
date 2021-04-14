package controllers
import com.google.inject.Inject
import models._
import play.api.i18n._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import play.api.mvc._
import repository.UserRepository
import utils.JsonFormat._
import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(cc: ControllerComponents,userRepository: UserRepository)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {
  def create: Action[JsValue] =
    Action.async(parse.json) { request =>
      request.body.validate[User].fold(error => Future.successful(BadRequest(JsError.toJson(error))), {user =>
        userRepository.create(user).map { isCreated =>
          Ok(Json.toJson(isCreated)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

  def getById: Action[JsValue] =
    Action.async(parse.json) { request =>
    println("hello"+ request.body)
      request.body.validate[UserData].fold(error =>{println(error); Future.successful(BadRequest(JsError.toJson(error)))}, {user =>
        userRepository.getById(user.email,user.password).map { userName =>
          val firstName = userName.get.firstName
          Ok(Json.toJson(firstName)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

}
