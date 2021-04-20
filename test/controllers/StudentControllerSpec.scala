package controllers

import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.StudentInfoRepository
import utils.JsonFormat._
import org.mockito.MockitoSugar
import utilities.SecureUser

import java.sql.Date
import scala.concurrent.{ExecutionContext, Future}


class StudentControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: StudentInfoRepository = mock[StudentInfoRepository]

  "StudentController " should {

    "get all student list" in new WithStudentApplication() {
      val student = List(Student("Aditya","aditya@gmail.com",1,Date.valueOf("1995-05-03"),"aditya@gmail.com",Some(1)),
        Student("Aman","aman@gmail.com",1,Date.valueOf("1994-08-03"),"aditya@gmail.com",Some(2)))
      when(mockedRepo.getAll()) thenReturn Future.successful(student)
      val result = studentController.getAll().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }
    "get all student list that created by reuestor" in new WithStudentApplication() {
      val student = Seq(("Aditya","aditya@gmail.com",Date.valueOf("1995-05-03"),"hcu",1),
        ("Aman","aman@gmail.com",Date.valueOf("1994-08-03"),"hcu",2))
      when(mockedRepo.getAllByCreator("aditya@gmail.com")) thenReturn Future.successful(student)
      val result = studentController.getAllByCreator().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "get all student list with their university" in new WithStudentApplication() {
      val student = Seq(("Aditya","aditya@gmail.com",Date.valueOf("1995-05-03"),"hcu",1),
        ("Aman","aman@gmail.com",Date.valueOf("1994-08-03"),"hcu",2))
      when(mockedRepo.joinStudentAndUniversity()) thenReturn Future.successful(student)
      val result = studentController.getStudentUniversity().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "get all university and number of students int it" in new WithStudentApplication() {
      val student = Seq(("hcu",2))
      when(mockedRepo.joinUniversityAndNumberOfStudent()) thenReturn Future.successful(student)
      val result = studentController.getUniversityCount().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "get student by id" in new WithStudentApplication() {
      val studentId = 1
      when(mockedRepo.getById(1)) thenReturn Future.successful(Some(Student("Aditya","aditya@gmail.com",1,Date.valueOf("1995-05-03"),"aditya@gmail.com",Some(1))))
      val result = studentController.getById(1).apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "create student" in new WithStudentApplication() {
      val student = Student("Abhinav","abhinav@gmail.com",1,Date.valueOf("1998-05-03"),"aditya@gmail.com")
      when(mockedRepo.create(student)) thenReturn Future.successful(3)
      val result = studentController.create().apply(FakeRequest().withBody(Json.toJson(student)))
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }
    "update student" in new WithStudentApplication() {
      val student = Student("Abhinav","abhinav@gmail.com",1,Date.valueOf("1995-05-03"),"aditya2gmail.com",Some(1))
      when(mockedRepo.update(student,"aditya@gmail.com")) thenReturn Future.successful(1)
      val result = studentController.update().apply(FakeRequest().withBody(Json.toJson(student)))
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "delete student" in new WithStudentApplication() {
      val studentId = 1;
      when(mockedRepo.delete(1,"aditya@gmail.com")) thenReturn Future.successful(1)
      val result = studentController.delete(1).apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

  }

}

class WithStudentApplication(implicit mockedRepo: StudentInfoRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  implicit val security : SecureUser = inject[SecureUser]
  val studentController: StudentController =
    new StudentController(
      stubControllerComponents(),
      security,
      mockedRepo
    )

}

