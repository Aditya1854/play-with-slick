package controllers
import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.UserRepository
import utils.JsonFormat._
import org.mockito.MockitoSugar
import utilities.JwtUtility

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}
class UserControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest{
  implicit val mockedRepo: UserRepository = mock[UserRepository]

  "UserController " should {

    "create user" in new WithUserApplication() {
      val user = User("aman","kumar","aman@gmail.com","654321")
      when(mockedRepo.create(user)) thenReturn Future.successful(1)
      val result = userController.create().apply(FakeRequest().withBody(Json.toJson(user)))
      status(result) mustBe OK
      val resultAsString = contentAsString(result)
      resultAsString mustBe """1"""
    }

    "validate user" in new WithUserApplication() {
      when(mockedRepo.getById("aditya@gmail.com","123456")) thenReturn Future.successful(Some(User("Aditya","Kumar","aditya@gmail.com","123456",Some(new Timestamp(1618945305939L)))))
      val result = userController.getById().apply(FakeRequest().withBody(Json.toJson(UserData("aditya@gmail.com","123456"))))
      status(result) mustBe OK
      val resultAsString = contentAsString(result)
//      resultAsString mustBe """{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTkwMzQzNTUsImlhdCI6MTYxODk0Nzk1NSwKICAiZmlyc3ROYW1lIiA6ICJuZWVyYWoiLAogICJlbWFpbCIgOiAibmVlcmFqQGdtYWlsLmNvbSIKfQ.eGDIzUI751W3N10c8S6D1zQwU6Rx8T2qEL3awZvD99s"}"""
    }

  }

}

class WithUserApplication(implicit mockedRepo: UserRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  implicit val jwt : JwtUtility = inject[JwtUtility]
  val userController: UserController =
    new UserController(
      stubControllerComponents(),
      jwt,
      mockedRepo
    )

}
