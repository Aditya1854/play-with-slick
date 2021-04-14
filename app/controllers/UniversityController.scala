package controllers
import com.google.inject.Inject
import models._
import play.api.i18n._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import play.api.mvc._
import repository.UniversityInfoRepository
import utils.JsonFormat._
import scala.concurrent.{ExecutionContext, Future}

class UniversityController @Inject()(cc: ControllerComponents,universityRepository: UniversityInfoRepository)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll: Action[AnyContent] =
    Action.async {
      universityRepository.getAllWithCounts().map { res =>
        val result = for(x<-res)
          yield(UniversityCounts(x._1.id,x._1.name,x._1.location,x._2))

        Ok(Json.toJson({result})).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def getList: Action[AnyContent] =
    Action.async {
      universityRepository.getAll().map { res =>
        Ok(Json.toJson({res})).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def create: Action[JsValue] =
    Action.async(parse.json) { request =>
      request.body.validate[University].fold(error => Future.successful(BadRequest(JsError.toJson(error))), {university =>
        universityRepository.create(university).map { _ =>
          Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }
  def getById(universityId :Int) :Action[AnyContent] ={
    Action.async{_=>
      universityRepository.getById(universityId).map{response =>
        Ok(Json.toJson(response)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }
  def delete(universityId: Int): Action[AnyContent] =
    Action.async { _ =>
      universityRepository.delete(universityId).map { response =>
        Ok(Json.toJson(response)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def update: Action[JsValue] = {
    println("updating the reuest .......")
    Action.async(parse.json) { request =>
	println("request is "+request.body)
      request.body.validate[University].fold(error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")), { university =>
println("university is "+university)
        universityRepository.update(university).map { isUniversityUpdated =>
          Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
//          if(isUniversityUpdated == 1)
//            Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
//          else Ok(Json.toJson(isUniversityUpdated)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

  }

}
