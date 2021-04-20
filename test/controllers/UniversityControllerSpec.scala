package controllers

import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.UniversityInfoRepository
import utils.JsonFormat._
import org.mockito.MockitoSugar
import utilities.SecureUser

import scala.concurrent.{ExecutionContext, Future}


class UniversityControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: UniversityInfoRepository = mock[UniversityInfoRepository]

  "UniversityController " should {

    "get all list with number of students" in new WithUniversityApplication() {
      val university = Seq((University(1,"hcu","hyderabad","aditya@gmail.com"),2))
      when(mockedRepo.getAllWithCounts()) thenReturn Future.successful(university)
      val result = universityController.getAll().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")

    }

    "get list of students that created by requestor" in new WithUniversityApplication() {
      val university = Seq((University(1,"hcu","hyderabad","aditya@gmail.com"),2))
      when(mockedRepo.getAllWithCreator("aditya@gmail.com")) thenReturn Future.successful(university)
      val result = universityController.getAllByCreator().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")

    }

//    "get all list with number of students" in new WithUniversityApplication() {
//      val university = Seq((University(1,"hcu","hyderabad","aditya@gmail.com"),2))
//      when(mockedRepo.getAllWithCounts()) thenReturn Future.successful(university)
//      val result = universityController.getAll().apply(FakeRequest())
//      val resultAsString = contentAsString(result)
//      resultAsString mustBe """[{"id":1,"name":"hcu","location":"hyderabad","email":"aditya@gmail.com","counts":2}]"""
//    }

    "get all list" in new WithUniversityApplication() {
      val university = List(University(1,"hcu","hyderabad","aditya@gmail.com"))
      when(mockedRepo.getAll()) thenReturn Future.successful(university)
      val result = universityController.getList().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "get university by id" in new WithUniversityApplication() {
      val universityId = 1
      when(mockedRepo.getById(1)) thenReturn Future.successful(Some(University(universityId,"hcu","hyderabad","aditya@gmail.com")))
      val result = universityController.getById(1).apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "create university" in new WithUniversityApplication() {
      val university = University(2,"bhu","varanasi","aditya@gmail.com")
      when(mockedRepo.create(university)) thenReturn Future.successful(1)
      val result = universityController.create().apply(FakeRequest().withBody(Json.toJson(university)))
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }
    "update university" in new WithUniversityApplication() {
      val university = University(1,"BRABU","hyderabad","aditya@gmail.com")
      when(mockedRepo.update(university,"aditya@gmail.com")) thenReturn Future.successful(1)
      val result = universityController.update().apply(FakeRequest().withBody(Json.toJson(university)))
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "delete university" in new WithUniversityApplication() {
      val universityId = 1;
      when(mockedRepo.delete(1,"aditya@gmail.com")) thenReturn Future.successful(1)
      val result = universityController.delete(universityId).apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

  }

}

class WithUniversityApplication(implicit mockedRepo: UniversityInfoRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  implicit val security : SecureUser = inject[SecureUser]
  val universityController: UniversityController =
    new UniversityController(
      stubControllerComponents(),
      security,
      mockedRepo
    )

}
