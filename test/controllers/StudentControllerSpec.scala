package controllers

import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.i18n.{DefaultLangs, MessagesApi}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.{StudentInfoRepository}
import utils.JsonFormat._
import org.mockito.MockitoSugar
import java.sql.Date

import scala.concurrent.{ExecutionContext, Future}


class StudentControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: StudentInfoRepository = mock[StudentInfoRepository]


  "StudentController " should {

    "get all student list" in new WithStudentApplication() {
      val student = List(Student("Aditya","aditya@gmail.com",1,Date.valueOf("1995-05-03"),Some(1)),
        Student("Aman","aman@gmail.com",1,Date.valueOf("1994-08-03"),Some(2)))
      when(mockedRepo.getAll()) thenReturn Future.successful(student)
      val result = studentController.getAll().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"name":"Aditya","email":"aditya@gmail.com","universityId":1,"DOB":799439400000,"id":1},{"name":"Aman","email":"aman@gmail.com","universityId":1,"DOB":775852200000,"id":2}]"""
    }
    "get all student list with their university" in new WithStudentApplication() {
      val student = Seq(("Aditya","aditya@gmail.com",Date.valueOf("1995-05-03"),"hcu",1),
        ("Aman","aman@gmail.com",Date.valueOf("1994-08-03"),"hcu",2))
      when(mockedRepo.joinStudentAndUniversity()) thenReturn Future.successful(student)
      val result = studentController.getStudentUniversity().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"name":"Aditya","email":"aditya@gmail.com","DOB":799439400000,"universityName":"hcu","id":1},{"name":"Aman","email":"aman@gmail.com","DOB":775852200000,"universityName":"hcu","id":2}]"""
    }
    "get all university and number of students int it" in new WithStudentApplication() {
      val student = Seq(("hcu",2))
      when(mockedRepo.joinUniversityAndNumberOfStudent()) thenReturn Future.successful(student)
      val result = studentController.getUniversityCount().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[["hcu",2]]"""
    }

    "get student by id" in new WithStudentApplication() {
      val studentId = 1
      when(mockedRepo.getById(1)) thenReturn Future.successful(Some(Student("Aditya","aditya@gmail.com",1,Date.valueOf("1995-05-03"),Some(1))))
      val result = studentController.getById(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"name":"Aditya","email":"aditya@gmail.com","universityId":1,"DOB":799439400000,"id":1}"""
    }

    "create student" in new WithStudentApplication() {
      val student = Student("Abhinav","abhinav@gmail.com",1,Date.valueOf("1998-05-03"))
      when(mockedRepo.create(student)) thenReturn Future.successful(3)
      val result = studentController.create().apply(FakeRequest().withBody(Json.toJson(student)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """3"""
    }
    "update student" in new WithStudentApplication() {
      val student = Student("Abhinav","abhinav@gmail.com",1,Date.valueOf("1995-05-03"),Some(1))
      when(mockedRepo.update(student)) thenReturn Future.successful(1)
      val result = studentController.update().apply(FakeRequest().withBody(Json.toJson(student)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """1"""
    }

    "delete student" in new WithStudentApplication() {
      val studentId = 1;
      when(mockedRepo.delete(1)) thenReturn Future.successful(1)
      val result = studentController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """1"""
    }

  }

}

class WithStudentApplication(implicit mockedRepo: StudentInfoRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  val studentController: StudentController =
    new StudentController(
      stubControllerComponents(),
      mockedRepo
    )

}

