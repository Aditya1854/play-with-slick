package repository

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import models.User
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import java.sql.Timestamp

@Singleton()
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserInfoTable with HasDatabaseConfigProvider[JdbcProfile] with StudentInfoTable {

  import profile.api._
  /*  create a user
   * */

  def create(user:User):Future[Int] = {db.run(userQuery += user)}

  /*  return user if exists according to given id
   * */

  def getById(email:String,password:String): Future[Option[User]] = {db.run(userQuery.filter(_.email === email).filter(_.password === password).result.headOption)}
}
private[repository] trait UserInfoTable {self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._
  private[UserInfoTable] class UserTable(tag : Tag) extends Table[User](tag, "user"){
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def email = column[String]("email",O.PrimaryKey)
    def password = column[String]("password")
    def time  = column[Timestamp]("time")
    def * = (firstName,lastName,email,password,time.?) <> (User.tupled, User.unapply)
  }
  lazy protected val userQuery = TableQuery[UserTable]
}
//CREATE TABLE ...
//your_date_column DATETIME DEFAULT CURRENT_TIMESTAMP
