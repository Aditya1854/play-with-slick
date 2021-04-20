package route

import models._
import org.h2.jdbc.JdbcSQLException
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import utils.JsonFormat._
import java.sql.Date

class RouteSpec extends PlaySpec with GuiceOneAppPerSuite {

  "Routes" should {

//    "request for get university list with number of students" in new WithApplication {
//      val result = route(app, FakeRequest(GET, "/university/")).get
//      status(result) mustBe OK
//      contentType(result) mustBe Some("application/json")
//      contentAsString(result) mustBe """[{"id":1,"name":"hcu","location":"hyderabad","counts":2}]"""
//    }
//    "request for get university list " in new WithApplication {
//      val result = route(app, FakeRequest(GET, "/university/list")).get
//      status(result) mustBe OK
//      contentType(result) mustBe Some("application/json")
//      contentAsString(result) mustBe """[{"id":1,"name":"hcu","location":"hyderabad"}]"""
//    }
//
//    "request for get university By Id" in new WithApplication {
//      val result = route(app, FakeRequest(GET, "/university/1")).get
//      status(result) mustBe OK
//      contentType(result) mustBe Some("application/json")
//      contentAsString(result) mustBe """{"id":1,"name":"hcu","location":"hyderabad"}"""
//    }
//    "request for create university" in new WithApplication {
//      val university = University(2, "bhu", "varanasi")
//      val result = route(app, FakeRequest(POST, "/university/").withBody(Json.toJson(university))).get
//      status(result) mustBe OK
//      contentType(result) mustBe Some("application/json")
//      contentAsString(result) mustBe """{"id":2,"name":"bhu","location":"varanasi"}"""
//    }
//    "request for update university" in new WithApplication {
//      val university = University(1, "hcu", "bihar")
//      val result = route(app, FakeRequest(PUT, "/university/").withBody(Json.toJson(university))).get
//      status(result) mustBe OK
//      contentType(result) mustBe Some("application/json")
//      contentAsString(result) mustBe """{"id":1,"name":"hcu","location":"bihar"}"""
//    }
//    "request for delete university" in new WithApplication {
//      val result = route(app, FakeRequest(DELETE, "/university/1")).get
//      whenReady(result.failed) {
//        isUniversityDeleted =>
//          intercept[JdbcSQLException] {
//            throw isUniversityDeleted
//          }
//      }
//    }
//    //student route
//    "Routes" should {
//

//      "request for get student list" in new WithApplication {
//        val result = route(app, FakeRequest(GET, "/student/").withHeaders(AUTHORIZATION :("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTkwMzQzNTUsImlhdCI6MTYxODk0Nzk1NSwKICAiZmlyc3ROYW1lIiA6ICJuZWVyYWoiLAogICJlbWFpbCIgOiAibmVlcmFqQGdtYWlsLmNvbSIKfQ.eGDIzUI751W3N10c8S6D1zQwU6Rx8T2qEL3awZvD99s"))).get
//        status(result) mustBe OK
//        contentType(result) mustBe Some("application/json")
//        contentAsString(result) mustBe """[{"name":"Aditya","email":"aditya@gmail.com","universityId":1,"DOB":799439400000,"id":1},{"name":"Aman","email":"aman@gmail.com","universityId":1,"DOB":775852200000,"id":2}]"""
//      }

//      "request for get student list with it's university name" in new WithApplication {
//        val result = route(app, FakeRequest(GET, "/student/university")).get
//        status(result) mustBe OK
//        contentType(result) mustBe Some("application/json")
//        contentAsString(result) mustBe """[{"name":"Aditya","email":"aditya@gmail.com","DOB":799439400000,"universityName":"hcu","id":1},{"name":"Aman","email":"aman@gmail.com","DOB":775852200000,"universityName":"hcu","id":2}]"""
//      }
//
//      "request for get student By Id" in new WithApplication {
//        val result = route(app, FakeRequest(GET, "/student/1")).get
//        status(result) mustBe OK
//        contentType(result) mustBe Some("application/json")
//        contentAsString(result) mustBe """{"name":"Aditya","email":"aditya@gmail.com","universityId":1,"DOB":799439400000,"id":1}"""
//      }
//      "request for create student" in new WithApplication {
//        val student = Student("vinit", "vinit@gmail.com", 1, Date.valueOf("1995-11-29"))
//        val result = route(app, FakeRequest(POST, "/student/").withBody(Json.toJson(student))).get
//        status(result) mustBe OK
//        contentType(result) mustBe Some("application/json")
//        contentAsString(result) mustBe """3"""
//      }
//      "request for update student" in new WithApplication {
//        val student = Student("Amit", "aditya@gmail.com", 1, Date.valueOf("1995-05-03"), Some(1))
//        val result = route(app, FakeRequest(PUT, "/student/").withBody(Json.toJson(student))).get
//        status(result) mustBe OK
//        contentType(result) mustBe Some("application/json")
//        contentAsString(result) mustBe """1"""
//      }
//      "request for delete student" in new WithApplication {
//        val result = route(app, FakeRequest(DELETE, "/student/1")).get
//        status(result) mustBe OK
//        contentType(result) mustBe Some("application/json")
//        contentAsString(result) mustBe """1"""
//      }
//
//    }
  }
}
