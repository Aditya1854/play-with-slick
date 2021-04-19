package repository
import javax.inject.{Inject, Singleton}
import models.University
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.Future


@Singleton()
class UniversityInfoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UniversityInfoTable with HasDatabaseConfigProvider[JdbcProfile] with StudentInfoTable{
  import profile.api._

  def getAll():Future[List[University]] = {db.run(universityQuery.to[List].result)}

  def create(university:University):Future[Int] = {db.run(universityQuery += university)}

  def update(university:University):Future[Int] = { db.run(universityQuery.filter(_.id === university.id).update(university))}

  def getById(id: Int): Future[Option[University]] = {db.run (universityQuery.filter(_.id === id).result.headOption)}

  def delete(id:Int):Future[Int] = {db.run(universityQuery.filter(_.id === id).delete)}

  def getAllWithCounts():Future[Seq[(University,Int)]]  = {
    val res = (for {(student, university) <- studentQuery join universityQuery on (_.universityId === _.id)}
      yield (student, university)).groupBy(_._2).map {
        case (university, len) => (university, len.map(_._1.universityId).length)
        }
     db.run(res.result)

  }

}

private[repository] trait UniversityInfoTable {self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._
  private[UniversityInfoTable] class UniversityTable(tag : Tag) extends Table[University](tag, "university"){
    def id = column[Int]("id",O.PrimaryKey)
    def name = column[String]("name")
    def location = column[String]("location")
    def * = (id,name,location) <> (University.tupled, University.unapply)
  }
  lazy protected val universityQuery = TableQuery[UniversityTable]
}
