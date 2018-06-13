package models


import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

/***/
case class UserRoleData(id: Int, userId: Int, roleId: Int)
class UserRoleTableDef(tag: Tag) extends Table[UserRoleData](tag, "user_roles") {
  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def userId = column[Int]("user_id")
  def roleId = column[Int]("role_id")
  override def * =
    (id, userId, roleId) <> (UserRoleData.tupled, UserRoleData.unapply)
}

/***/
case class UserRoleForm(userId: Int, roleId: Int)
class UserRoleFormDef(tag: Tag) extends Table[UserRoleForm](tag, "user_roles") {
  def userId = column[Int]("user_id")
  def roleId = column[Int]("role_id")
  override def * =
    (userId, roleId) <>(UserRoleForm.tupled, UserRoleForm.unapply)
}

/***/
class UserRole @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                            (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  private val UserRole  = TableQuery[UserRoleFormDef]

  private val UserData = TableQuery[UserRoleTableDef]

  def insert(userRoleForm: UserRoleForm): Future[String] = db.run(UserRole += userRoleForm).map {
    res => "User successfully added"
  }.recover{
    case ex: Exception => ex.getCause.getMessage
  }

  def deleteByUserId(userId: Int): Future[Int] = {
    db.run(UserRole.filter(_.userId === userId).delete)
  }
}