package models


import javax.inject.Inject

import be.objectify.deadbolt.scala.models.Subject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import security.{SecurityPermission, SecurityRole}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.collection.mutable.ListBuffer

case class LoginForm(username: String, password: String)
object LoginForm {
  implicit val reader = Json.reads[LoginForm]
}

/***/
case class UserData(id: Int, username: String, password: String, email: String, created_at: Option[Int], updated_at: Option[Int], created_by: Option[Int], updated_by: Option[Int])
class UserTableDef(tag: Tag) extends Table[UserData](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def username = column[String]("username")
  def password = column[String]("password")
  def email = column[String]("email")
  def created_at = column[Option[Int]]("created_at")
  def updated_at = column[Option[Int]]("updated_at")
  def created_by = column[Option[Int]]("created_by")
  def updated_by = column[Option[Int]]("updated_by")
  override def * =
    (id, username, password, email, created_at, updated_at, created_by, updated_by) <>(UserData.tupled, UserData.unapply)
}

/***/
case class UserForm(var username: String, password: String, email: String)
//class UserFormDef(tag: Tag) extends Table[UserForm](tag, "users") {
//
//  def username = column[String]("username")
//  def password = column[String]("password")
//  def email = column[String]("email")
//  override def * =
//    (username, password, email) <> (UserForm.tupled, UserForm.unapply)
//}

/***/
class UserAuth(username: String, roleList: List[SecurityRole], permissionList: List[SecurityPermission]) extends Subject {
  override def roles: List[SecurityRole] = roleList
//    List(SecurityRole("foo"),
//      SecurityRole("bar"))

  override def permissions: List[SecurityPermission] = permissionList
//    List(SecurityPermission("printers.edit"))

  override def identifier: String = username
}

/***/
class User @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                               (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

//  private val UserForm  = TableQuery[UserFormDef]

  private val UserTable  = TableQuery[UserTableDef]

  def getAuthInfo(username: String): Future[Option[UserAuth]] = {
    val role = TableQuery[RoleTableDef]
    val permission = TableQuery[PermissionTableDef]
    val userRole = TableQuery[UserRoleTableDef]
    val rolePermission = TableQuery[RolePermissionFormDef]


    val q = (UserTable join userRole on(_.id === _.userId) join role on(_._2.roleId === _.id) join
      rolePermission on (_._1._2.roleId === _.roleId) join permission on (_._2.permissionId === _.id)).filter(_._1._1._1._1.username === username).result
    q.statements.foreach(println) // print query
    val rs = db.run {
      q
    }

    rs.map {
      list => {
        list.size match {
          case 0 => None
          case _ => {
            val username = list.head._1._1._1._1.username
            val roles = ListBuffer.empty[SecurityRole]
            val permissions = ListBuffer.empty[SecurityPermission]
            list.foreach {
              item =>{
                val ((((user, userRole), role), rolePermission), permission) = item
                roles += SecurityRole(role.code)
                permissions += SecurityPermission(permission.code)
              }
            }

            Some(new UserAuth(username, roles.toList, permissions.toList))
          }
        }

      }
    }
  }

  def insert(userForm: UserForm): Future[Int] = {
    userForm.username = "12121"
//    userForm.copy(username = "sdfsdf")
//    db.run(UserForm += userForm)
    Future(1)
  }

  def delete(userId: Int): Unit = {
    db.run(UserTable.filter(_.id === userId).delete)
  }
}
