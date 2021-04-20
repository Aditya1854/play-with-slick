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

  /*  return oll university data with number of students after joining student and university table
   * */

  def getAll: Action[AnyContent] =
    security.async {
      universityRepository.getAllWithCounts().map { res =>
        val result = for(x<-res)
          yield UniversityCounts(x._1.id,x._1.name,x._1.location,x._2)

        Ok(Json.toJson({result})).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  /*  return return oll university data with number of students after joining student and university table that are created by the requested user
 * */

  def getAllByCreator: Action[AnyContent] =
    security.async {request =>
      universityRepository.getAllWithCreator(request.user.email).map { res =>
        val result = for(x<-res)
          yield UniversityCounts(x._1.id,x._1.name,x._1.location,x._2)

        Ok(Json.toJson({result})).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  /*  return oll university data  in a list
     * */

  def getList: Action[AnyContent] =
    security.async {
      universityRepository.getAll().map { res =>
        Ok(Json.toJson({res})).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  /*  create a new university
   * */

  def create: Action[JsValue] =
    security.async(parse.json) { request =>
      request.body.validate[UniversityData].fold(error => Future.successful(BadRequest(JsError.toJson(error))), {universityData =>
        val university = University(universityData.id,universityData.name,universityData.location,request.user.email)
        universityRepository.create(university).map { _ =>
          Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

  /*  return  university data with given id
   * */

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

  /*  delete university record
   * */

  def delete(universityId: Int): Action[AnyContent] =
    security.async { request =>
      universityRepository.delete(universityId,request.user.email).map { response =>
        Ok(Json.toJson(response)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  /*  update/edit university data in a row
   * */

  def update: Action[JsValue] = {
    security.async(parse.json) { request =>
      request.body.validate[UniversityData].fold(error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")), { universityData =>
        val university = University(universityData.id,universityData.name,universityData.location,request.user.email)
        universityRepository.update(university,request.user.email).map { _ =>
          Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
     }
      })
    }

  }

}
