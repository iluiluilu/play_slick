package services

import javax.inject.Inject

import models.{LoginForm, User, UserForm}
import play.api.mvc.Result
import utils.JS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UserService @Inject() (user: User) {




//  def all(): Future[Seq[UserFormData]] = db.run(Users.result)


  def insert(userForm: UserForm): Future[Int] = user.insert(userForm)



  def login(loginForm: LoginForm): Future[Result] = {

    val userAuth = user.getAuthInfo(loginForm.username)
    userAuth.map {
      case Some(x) => JS.OK("reason" -> "xxx").withSession("username" -> x.identifier)
      case None => JS.KO("Sai tên đăng nhập hoặc mật khẩu!")
    }
  }

}
