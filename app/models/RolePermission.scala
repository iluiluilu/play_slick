package models


import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

/***/
case class RolePermissionData(id: Int, roleId: Int, permissionId: Int)
class RolePermissionTableDef(tag: Tag) extends Table[RolePermissionData](tag, "role_permissions") {
  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def roleId = column[Int]("role_id")
  def permissionId = column[Int]("permission_id")
  override def * =
    (id, roleId, permissionId) <>(RolePermissionData.tupled, RolePermissionData.unapply)
}

/***/
case class RolePermissionForm(userId: Int, roleId: Int)
class RolePermissionFormDef(tag: Tag) extends Table[RolePermissionForm](tag, "role_permissions") {
  def roleId = column[Int]("role_id")
  def permissionId = column[Int]("permission_id")
  override def * =
    (roleId, permissionId) <>(RolePermissionForm.tupled, RolePermissionForm.unapply)
}

/***/
class RolePermission @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                         (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  private val RolePermission  = TableQuery[RolePermissionFormDef]

  def insert(rolePermissionForm: RolePermissionForm): Future[String] = db.run(RolePermission += rolePermissionForm).map {
    res => "User successfully added"
  }.recover{
    case ex: Exception => ex.getCause.getMessage
  }

  def deleteByRoleId(roleId: Int): Future[Int] = {
    db.run(RolePermission.filter(_.roleId === roleId).delete)
  }

  def deleteByPermissionId(permissionId: Int): Future[Int] = {
    db.run(RolePermission.filter(_.permissionId === permissionId).delete)
  }
}