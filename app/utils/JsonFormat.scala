package utils



import models._
import play.api.libs.json.{Json, Reads, Writes}
import java.sql.Timestamp


object JsonFormat {
  implicit val timestampReads: Reads[Timestamp] = {
    implicitly[Reads[Long]].map(new Timestamp(_))
  }

  implicit val timestampWrites: Writes[Timestamp] = {
    implicitly[Writes[Long]].contramap(_.getTime)
  }
  implicit val universityFormat = Json.format[University]
  implicit val studentFormat = Json.format[Student]
  implicit val universityCounts = Json.format[UniversityCounts]
  implicit val studentWithUniversityName = Json.format[StudentWithUniversityName]
  implicit val userVal = Json.format[User]
  implicit val userData = Json.format[UserData]


}


