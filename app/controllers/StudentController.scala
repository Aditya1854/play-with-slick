package controllers
import com.google.inject.Inject
import models._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import repository.StudentInfoRepository
import utils.JsonFormat._
import scala.concurrent.{ExecutionContext, Future}
import utilities.SecureUser

class StudentController @Inject()(cc: ControllerComponents,security : SecureUser,studentRepository: StudentInfoRepository)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll: Action[AnyContent] =
    security.async {
      studentRepository.getAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def getStudentUniversity: Action[AnyContent] =
    security.async {
      studentRepository.joinStudentAndUniversity().map { res =>
        val result = for(x<-res)
          yield StudentWithUniversityName(x._1,x._2,x._3,x._4,Some(x._5))
        Ok(Json.toJson(result)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  def getUniversityCount: Action[AnyContent] =
    security.async {
      studentRepository.joinUniversityAndNumberOfStudent().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def create: Action[JsValue] =

    security.async(parse.json) { request =>
      request.body.validate[Student].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { student =>
        studentRepository.create(student).map { createStudentId =>
          Ok(Json.toJson(createStudentId)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }
  def getById(studentId :Int) :Action[AnyContent] ={
    security.async{_=>
      studentRepository.getById(studentId).map{
        case Some(student) => Ok(Json.toJson(student)).withHeaders("Access-Control-Allow-Origin" -> "*")
        case None =>
          BadRequest("Email does not exist").withHeaders("Access-Control-Allow-Origin" -> "*")

      }
    }
  }
  def delete(studentId: Int): Action[AnyContent] =
    security.async{ _ =>
      studentRepository.delete(studentId).map { response =>
        Ok(Json.toJson(response)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def update: Action[JsValue] =
    security.async(parse.json) { request =>
      request.body.validate[Student].fold(error => {Future.successful(BadRequest(JsError.toJson(error)))}, { student =>
        studentRepository.update(student).map { isStudentUpdated =>
              Ok(Json.toJson(isStudentUpdated)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

}
