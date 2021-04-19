package utils



import models._
import play.api.libs.json.{Json, OFormat, Reads, Writes}

import java.sql.Timestamp


object JsonFormat {
  implicit val timestampReads: Reads[Timestamp] = {
    implicitly[Reads[Long]].map(new Timestamp(_))
  }

  implicit val timestampWrites: Writes[Timestamp] = {
    implicitly[Writes[Long]].contramap(_.getTime)
  }
  implicit val universityFormat :OFormat[University] = Json.format[University]
  implicit val studentFormat :OFormat[Student] = Json.format[Student]
  implicit val universityCounts :OFormat[UniversityCounts] = Json.format[UniversityCounts]
  implicit val studentWithUniversityName :OFormat[StudentWithUniversityName] = Json.format[StudentWithUniversityName]
  implicit val userVal :OFormat[User] = Json.format[User]
  implicit val userData :OFormat[UserData] = Json.format[UserData]
  implicit val userName :OFormat[UserName] = Json.format[UserName]


}


