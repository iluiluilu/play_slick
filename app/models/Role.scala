package models


import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

/***/
case class RoleData(id: Int, code: String, name: String)
class RoleTableDef(tag: Tag) extends Table[RoleData](tag, "roles") {
  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def code = column[String]("code")
  def name = column[String]("name")
  override def * =
    (id, code, name) <> (RoleData.tupled, RoleData.unapply)
}

/***/
case class RoleForm(code: String, name: String)
class RoleFormDef(tag: Tag) extends Table[RoleForm](tag, "roles") {
  def code = column[String]("code")
  def name = column[String]("name")
  override def * =
    (code, name) <> (RoleForm.tupled, RoleForm.unapply)
}

/***/
class DRole @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                     (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  private val RoleForm = TableQuery[RoleFormDef]

  private val RoleData = TableQuery[RoleTableDef]

  def insert(role: RoleForm): Future[String] = db.run(RoleForm += role).map {
    res => "User successfully added"
  }.recover{
    case ex: Exception => ex.getCause.getMessage
  }

  def deleteById(id: Int): Future[Int] = {
    db.run(RoleData.filter(_.id === id).delete)
  }
}