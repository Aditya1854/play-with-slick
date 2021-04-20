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
/*  return all student data
* */

  def getAll: Action[AnyContent] =
    security.async {
      studentRepository.getAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  /*  return only the student data that are created by the requested user
  * */
  def getAllByCreator: Action[AnyContent] =
    security.async {userRequest => {
      studentRepository.getAllByCreator(userRequest.user.email).map { res =>
        val result = for(x<-res)
          yield StudentWithUniversityName(x._1,x._2,x._3,x._4,Some(x._5))
        Ok(Json.toJson(result)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
    }

  /*  return all student data  with university name in student table there is no university name
  so after performing join it returns
* */
  def getStudentUniversity: Action[AnyContent] =
    security.async {
      studentRepository.joinStudentAndUniversity().map { res =>
        val result = for(x<-res)
          yield StudentWithUniversityName(x._1,x._2,x._3,x._4,Some(x._5))
        Ok(Json.toJson(result)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  /*  return university name and number of student in it
* */
  def getUniversityCount: Action[AnyContent] =
    security.async {
      studentRepository.joinUniversityAndNumberOfStudent().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  /*  create student data
* */
  def create: Action[JsValue] =

    security.async(parse.json) { request =>
      request.body.validate[StudentData].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { studentData =>
        val student = Student(studentData.name,studentData.email,studentData.universityId,studentData.DOB,request.user.email)
        studentRepository.create(student).map { createStudentId =>
          Ok(Json.toJson(createStudentId)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

  /*  return student data  by id
* */
  def getById(studentId :Int) :Action[AnyContent] ={
    security.async{_=>
      studentRepository.getById(studentId).map{
        case Some(student) => Ok(Json.toJson(student)).withHeaders("Access-Control-Allow-Origin" -> "*")
        case None =>
          BadRequest("Email does not exist").withHeaders("Access-Control-Allow-Origin" -> "*")

      }
    }
  }

  /*  delete student record
* */
  def delete(studentId: Int): Action[AnyContent] =
    security.async{ request =>
      studentRepository.delete(studentId,request.user.email).map { response =>
        Ok(Json.toJson(response)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  /*  update student record
* */
  def update: Action[JsValue] =
    security.async(parse.json) { request =>
      request.body.validate[StudentData].fold(error => {Future.successful(BadRequest(JsError.toJson(error)))}, { studentData =>
        val student = Student(studentData.name,studentData.email,studentData.universityId,studentData.DOB,request.user.email,studentData.id)
        studentRepository.update(student,request.user.email).map { isStudentUpdated =>
              Ok(Json.toJson(isStudentUpdated)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

}
