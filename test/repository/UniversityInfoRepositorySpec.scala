package repository

import org.h2.jdbc.JdbcSQLException
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication}
import org.scalatest.concurrent.ScalaFutures.whenReady



class UniversityInfoRepositorySpec extends PlaySpec with GuiceOneAppPerTest {


  import models._

  "University repository" should {
    "get all university list with number of students" in new WithUniversityRepository() {
      val result = await(universityRepo.getAllWithCounts())
      result.length mustBe 1
      result mustBe Vector((University(1,"hcu","hyderabad","aditya@gmail.com"),2))
    }

    "get all university list" in new WithUniversityRepository() {
      val result = await(universityRepo.getAll())
      result.length mustBe 1
      result.head.name mustBe "hcu"
    }


    "get university by ID" in new WithUniversityRepository() {
      val result = await(universityRepo.getById(1))
      result.isDefined mustBe true
      result.get mustBe University(1,"hcu","hyderabad","aditya@gmail.com")


    }

    "create a row" in new WithUniversityRepository() {
      val university =University(2,"BRABU","bihar","aditya@gmail.com")
      val isUniversityCreated = await(universityRepo.create(university))
      isUniversityCreated mustBe 1
    }


    "update a row" in new WithUniversityRepository() {
      val university = University(1,"JNTU","hyderabad","aditya@gmail.com")
      val result = await(universityRepo.update(university,"aditya@gmail.com"))
      result mustBe 1
    }

    "delete a row" in new WithUniversityRepository() {
      val result = universityRepo.delete(1,"aditya@gmail.com")
	whenReady(result.failed) {
        isUniversityDeleted =>
          intercept[JdbcSQLException] {
            throw isUniversityDeleted
          }
      }

    }

  }

}


trait WithUniversityRepository extends WithApplication with Injecting {

  val universityRepo = inject[UniversityInfoRepository]
}
