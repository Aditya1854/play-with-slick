package repository
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication}

import java.sql.Date
import java.sql.Date
//


class StudentInfoRepositorySpec extends PlaySpec with GuiceOneAppPerTest {
 

  import models._

  "Student repository" should {

    "get all rows" in new WithStudentRepository() {
      val result = await(studentRepo.getAll())
      result.length mustBe 2
      result.head.name mustBe "Aditya"
    }

    "get student and there corresponding university" in new WithStudentRepository() {
      val result = await(studentRepo.joinStudentAndUniversity())
      result.length mustBe 2
      result mustBe Vector(("Aditya","aditya@gmail.com",Date.valueOf("1995-05-03"),"hcu",1),("Aman","aman@gmail.com",Date.valueOf("1994-08-03"),"hcu",2))
    }

    "get university and number of student in it" in new WithStudentRepository() {
      val result = await(studentRepo.joinUniversityAndNumberOfStudent())
      result mustBe List(("hcu",2))
    }

    "get single rows" in new WithStudentRepository() {
      val result = await(studentRepo.getById(1))
      result.isDefined mustBe true
      result.get mustBe Student("Aditya","aditya@gmail.com",1,Date.valueOf("1995-05-03"),Some(1))


    }

    "create a row" in new WithStudentRepository() {
      val student = Student("Abhijit","abhijit@gmail.com",1,Date.valueOf("1996-02-06"))
      val studentId = await(studentRepo.create(student))
      studentId mustBe 3
    }


    "update a row" in new WithStudentRepository() {
      val result = await(studentRepo.update(Student("Aditya","aditya@gmail.com",1,Date.valueOf("2005-05-03"),Some(1))))
      result mustBe 1
    }

    "delete a row" in new WithStudentRepository() {
      val result = await(studentRepo.delete(1))
      result mustBe 1
    }

}


}


trait WithStudentRepository extends WithApplication with Injecting {

  val studentRepo = inject[StudentInfoRepository]
}
