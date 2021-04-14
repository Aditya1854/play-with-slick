package controllers

import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.i18n.{DefaultLangs, MessagesApi}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.UniversityInfoRepository
import utils.JsonFormat._
import org.mockito.MockitoSugar



import scala.concurrent.{ExecutionContext, Future}


class UniversityControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: UniversityInfoRepository = mock[UniversityInfoRepository]


  "UniversityController " should {

    "get all list with number of students" in new WithUniversityApplication() {
      val university = Seq((University(1,"hcu","hyderabad"),2))
      when(mockedRepo.getAllWithCounts()) thenReturn Future.successful(university)
      val result = universityController.getAll().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"id":1,"name":"hcu","location":"hyderabad","counts":2}]"""
    }

    "get all list" in new WithUniversityApplication() {
      val university = List(University(1,"hcu","hyderabad"))
      when(mockedRepo.getAll()) thenReturn Future.successful(university)
      val result = universityController.getList().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"id":1,"name":"hcu","location":"hyderabad"}]"""
    }

    "get university by id" in new WithUniversityApplication() {
      val universityId = 1
      when(mockedRepo.getById(1)) thenReturn Future.successful(Some(University(1,"hcu","hyderabad")))
      val result = universityController.getById(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"id":1,"name":"hcu","location":"hyderabad"}"""
    }

    "create university" in new WithUniversityApplication() {
      val university = University(2,"bhu","varanasi")
      when(mockedRepo.create(university)) thenReturn Future.successful(1)
      val result = universityController.create().apply(FakeRequest().withBody(Json.toJson(university)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"id":2,"name":"bhu","location":"varanasi"}"""
    }
    "update university" in new WithUniversityApplication() {
      val university = University(1,"BRABU","hyderabad")
      when(mockedRepo.update(university)) thenReturn Future.successful(1)
      val result = universityController.update().apply(FakeRequest().withBody(Json.toJson(university)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"id":1,"name":"BRABU","location":"hyderabad"}"""
    }

    "delete university" in new WithUniversityApplication() {
      val universityId = 1;
      when(mockedRepo.delete(1)) thenReturn Future.successful(1)
      val result = universityController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """1"""
    }

  }

}

class WithUniversityApplication(implicit mockedRepo: UniversityInfoRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  val universityController: UniversityController =
    new UniversityController(
      stubControllerComponents(),
      mockedRepo
    )

}
