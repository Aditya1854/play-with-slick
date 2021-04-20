package repository
import javax.inject.{Inject, Singleton}
import models.Student
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.sql.Date
import scala.concurrent.Future

@Singleton()
class StudentInfoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends StudentInfoTable with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  /*  return oll student data in a list
    * */

  def getAll():Future[List[Student]] = {
    db.run(studentQuery.to[List].result)
  }

  /*  return student data with university name after joining student and university table those are created by requested user
   * */

  def getAllByCreator(creator : String):Future[Seq[(String,String,Date,String,Int)]] = {
    val res = for {
      student <- studentQuery if (student.userEmail === creator)
      university <- universityQuery if student.universityId === university.id
    } yield (student.name,student.email,student.DOB, university.name,student.id)
    db.run(res.result)
  }

  def create(student:Student):Future[Int] = {db.run(autoIncrementStudentId += student)}

  def update(student:Student,requestor:String):Future[Int] = {db.run(studentQuery.filter(_.id === student.id).filter(_.userEmail === requestor).update(student))}

  def getById(id: Int): Future[Option[Student]] = {db.run(studentQuery.filter(_.id === id).result.headOption)}

  def delete(id:Int,requestor:String):Future[Int] = {db.run(studentQuery.filter(_.id === id).filter(_.userEmail === requestor).delete)}

  /*  return student data with university name after joining student and university table
   * */

  def joinStudentAndUniversity():Future[Seq[(String,String,Date,String,Int)]] ={
    val res = for {(student, university) <- studentQuery join universityQuery on (_.universityId === _.id)}
      yield (student.name,student.email,student.DOB, university.name,student.id)
    db.run(res.result)
  }

  /*  return student and their corresponding university name
   * */

  def joinUniversityAndNumberOfStudent():Future[Seq[(String,Int)]] = {
    val res = (for {(student, university) <- studentQuery join universityQuery on (_.universityId === _.id)}
      yield (student, university)).groupBy(_._2.name).map {
      case (university, len) => (university, len.map(_._1.universityId).length)
    }
    db.run(res.result)
  }

}
private[repository] trait StudentInfoTable extends UniversityInfoTable {self : HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._
  private[StudentInfoTable] class StudentTable(tag:Tag) extends Table[Student](tag,"student"){
    def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email",O.Unique)
    def universityId = column[Int]("university_id")
    def DOB = column[Date]("DOB")
    def userEmail = column[String]("user_email")
    def * = (name,email,universityId,DOB,userEmail,id.?) <> (Student.tupled, Student.unapply)
    def fk = foreignKey("key2", universityId, universityQuery)(_.id)
    def fkTwo = foreignKey("key3", userEmail, userQuery)(_.email)
  }
  lazy protected val studentQuery = TableQuery[StudentTable]
  protected def autoIncrementStudentId = studentQuery returning studentQuery.map(_.id)
}