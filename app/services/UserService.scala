package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import models.{User, UserFormData}

import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._

  private val Users  = TableQuery[UsersTable]

  def all(): Future[Seq[UserFormData]] = db.run(Users.result)

  def insert(userFormData: UserFormData): Future[Unit] = db.run(Users += userFormData).map { _ => () }


  private class UsersTable(tag: Tag) extends Table[UserFormData](tag, "users") {

//    def username = column[String]("username", O.PrimaryKey)
    def username = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")

    def * = (username, password, email) <> (UserFormData.tupled, UserFormData.unapply)
  }
}
