package repository

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import play.api.test.{Injecting, WithApplication}

import java.sql.Timestamp

class UserRepositorySpec extends PlaySpec with GuiceOneAppPerTest {
  import models._
  "User repository" should {

    "create a user" in new WithUserRepository() {
      val result = await(userRepo.create(User("aman","kumar","aman@gmail.com","654321")))
      result mustBe 1
    }

    "validate a user" in new WithUserRepository() {
      val result = await(userRepo.getById("aditya@gmail.com","123456"))
      result mustBe Some(User("Aditya","Kumar","aditya@gmail.com","123456",Some(new Timestamp(1618945305939L))))
    }
  }
}
trait WithUserRepository extends WithApplication with Injecting {

  val userRepo = inject[UserRepository]
}