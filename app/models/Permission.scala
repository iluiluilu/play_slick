package models


import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

/***/
case class PermissionData(id: Int, code: String, name: String)
class PermissionTableDef(tag: Tag) extends Table[PermissionData](tag, "permissions") {
  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def code = column[String]("code")
  def name = column[String]("name")
  override def * =
    (id, code, name) <> (PermissionData.tupled, PermissionData.unapply)
}

/***/
case class PermissionForm(code: String, name: String)
class PermissionFormDef(tag: Tag) extends Table[PermissionForm](tag, "permissions") {
  def code = column[String]("code")
  def name = column[String]("name")
  override def * =
    (code, name) <> (PermissionForm.tupled, PermissionForm.unapply)
}

/***/
class Permission @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                     (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  private val PermissionForm = TableQuery[PermissionFormDef]

  private val PermissionData = TableQuery[PermissionTableDef]

  def insert(permission: PermissionForm): Future[String] = db.run(PermissionForm += permission).map {
    res => "User successfully added"
  }.recover{
    case ex: Exception => ex.getCause.getMessage
  }

  def deleteById(id: Int): Future[Int] = {
    db.run(PermissionData.filter(_.id === id).delete)
  }
}