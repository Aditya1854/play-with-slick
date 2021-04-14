package models
import java.sql.Date
import java.sql.Timestamp


case class University( id:Int,name:String,location:String)
case class Student(name:String,email:String,universityId :Int,DOB:Date,id:Option[Int]=None)
case class UniversityCounts( id:Int,name:String,location:String,counts:Int)
case class StudentWithUniversityName(name:String,email:String,DOB:Date,universityName :String,id:Option[Int]=None)
case class User(firstName : String,lastName : String, email :String,password:String,time:Option[Timestamp])
case class UserData(email:String,password:String)

