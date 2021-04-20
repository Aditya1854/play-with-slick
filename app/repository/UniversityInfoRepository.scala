package repository
import javax.inject.{Inject, Singleton}
import models.University
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.Future


@Singleton()
class UniversityInfoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UniversityInfoTable with HasDatabaseConfigProvider[JdbcProfile] with StudentInfoTable{
  import profile.api._

  /*  return all university data in a list
   * */

  def getAll():Future[List[University]] = {db.run(universityQuery.to[List].result)}

  def create(university:University):Future[Int] = {db.run(universityQuery += university)}

  def update(university:University,requestor:String):Future[Int] = { db.run(universityQuery.filter(_.id === university.id).filter(_.email === requestor).update(university))}

  def getById(id: Int): Future[Option[University]] = {db.run (universityQuery.filter(_.id === id).result.headOption)}

  def delete(id:Int,requestor:String):Future[Int] = {db.run(universityQuery.filter(_.id === id).filter(_.email === requestor).delete)}

  /*  get all university data with number of students in it
   * */

  def getAllWithCounts():Future[Seq[(University,Int)]]  = {
    val res = (for {(student, university) <- studentQuery join universityQuery on (_.universityId === _.id)}
      yield (student, university)).groupBy(_._2).map {
        case (university, len) => (university, len.map(_._1.universityId).length)
        }
     db.run(res.result)

  }

  /*  get university data with number of students in it that are created by the requestor
   * */
  def getAllWithCreator(requestor:String):Future[Seq[(University,Int)]]  = {
    val res = (for {
      university <- universityQuery if university.email === requestor
      student <- studentQuery if student.universityId === university.id
    } yield (student, university)).groupBy(_._2).map {
      case (university, len) => (university, len.map(_._1.universityId).length)
    }
    db.run(res.result)

  }

}

private[repository] trait UniversityInfoTable extends UserInfoTable {self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._
  private[UniversityInfoTable] class UniversityTable(tag : Tag) extends Table[University](tag, "university"){
    def id = column[Int]("id",O.PrimaryKey)
    def name = column[String]("name")
    def location = column[String]("location")
    def email = column[String]("email")
    def * = (id,name,location,email) <> (University.tupled, University.unapply)
    def fk = foreignKey("key1", email, userQuery)(_.email)
  }
  lazy protected val universityQuery = TableQuery[UniversityTable]
}
