package models


import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._


case class UserData(id: Int, username: String, password: String, email: String, created_at: Int, updated_at: Int, created_by: Int, updated_by: Int)
class UserTableDef(tag: Tag) extends Table[UserData](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def username = column[String]("username")
  def password = column[String]("password")
  def email = column[String]("email")
  def created_at = column[Int]("created_at")
  def updated_at = column[Int]("updated_at")
  def created_by = column[Int]("created_by")
  def updated_by = column[Int]("updated_by")
  override def * =
    (id, username, password, email, created_at, updated_at, created_by, updated_by) <>(UserData.tupled, UserData.unapply)
}


case class UserForm(username: String, password: String, email: String)
class UserFormDef(tag: Tag) extends Table[UserForm](tag, "users") {

  def username = column[String]("username")
  def password = column[String]("password")
  def email = column[String]("email")
  override def * =
    (username, password, email) <> (UserForm.tupled, UserForm.unapply)
}


class User @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                               (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  private val UserForm  = TableQuery[UserFormDef]

  private val UserTable  = TableQuery[UserTableDef]

  def getAuthInfo(userId: Int): Unit = {
    val role = TableQuery[RoleTableDef]
    val permission = TableQuery[PermissionTableDef]
    val userRole = TableQuery[UserRoleTableDef]
    val rolePermission = TableQuery[RolePermissionFormDef]


    val q = (UserTable join userRole on(_.id === _.userId) join role on(_._2.roleId === _.id) join
      rolePermission on (_._1._2.roleId === _.roleId) join permission on (_._2.permissionId === _.id)).filter(_._1._1._1._1.id === userId).result
    q.statements.foreach(println)
    val x = db.run{
      q
    }
    x.onComplete{
      f => {
        println(f)
      }
    }
    x.foreach{
      rs => rs.foreach{
        item =>{
          val ((((x, y), z), a), b) = item
          println(x)
          println(y)
          println(z)
          println(a)
          println(b)
        }
      }
    }
  }

  def insert(userForm: UserForm): Future[Int] = db.run(UserForm += userForm)

  def delete(userId: Int): Unit = {
    db.run(UserTable.filter(_.id === userId).delete)
  }
}





/*
class User(username: String, userRoles: List[String], userPermissions: List[String]) extends Subject {
  override def identifier: String = username

  override def roles: List[SecurityRole] = {

    var roleList = new ListBuffer[SecurityRole]()
    userRoles.foreach(r => roleList += SecurityRole(r))

    roleList.toList
  }



  override def permissions: List[SecurityPermission] = {

    var permissionList = new ListBuffer[SecurityPermission]()
    userPermissions.foreach(p => permissionList += SecurityPermission(p))

    permissionList.toList
  }
}*/
