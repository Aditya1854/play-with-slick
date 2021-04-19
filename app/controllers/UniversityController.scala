package controllers
import com.google.inject.Inject
import models._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import repository.UniversityInfoRepository
import utils.JsonFormat._
import scala.concurrent.{ExecutionContext, Future}
import utilities.SecureUser

class UniversityController @Inject()(cc: ControllerComponents, security : SecureUser, universityRepository: UniversityInfoRepository)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll: Action[AnyContent] =
    security.async {
      universityRepository.getAllWithCounts().map { res =>
        val result = for(x<-res)
          yield UniversityCounts(x._1.id,x._1.name,x._1.location,x._2)

        Ok(Json.toJson({result})).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def getList: Action[AnyContent] =
    security.async {
      universityRepository.getAll().map { res =>
        Ok(Json.toJson({res})).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def create: Action[JsValue] =
    security.async(parse.json) { request =>
      request.body.validate[University].fold(error => Future.successful(BadRequest(JsError.toJson(error))), {university =>
        universityRepository.create(university).map { _ =>
          Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }
  def getById(universityId :Int) :Action[AnyContent] ={
    security.async{_=>
      universityRepository.getById(universityId).map{
        case Some(response) =>
          Ok(Json.toJson(response)).withHeaders("Access-Control-Allow-Origin" -> "*")
        case None =>
          BadRequest("Email does not exist").withHeaders("Access-Control-Allow-Origin" -> "*")


      }
    }
  }
  def delete(universityId: Int): Action[AnyContent] =
    security.async { _ =>
      universityRepository.delete(universityId).map { response =>
        Ok(Json.toJson(response)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def update: Action[JsValue] = {
    security.async(parse.json) { request =>
      request.body.validate[University].fold(error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")), { university =>
        universityRepository.update(university).map { _ =>
          Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
     }
      })
    }

  }

}
