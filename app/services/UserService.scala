package services

import javax.inject.Inject

import models.{User, UserForm}

import scala.concurrent.Future


class UserService @Inject() (user: User) {




//  def all(): Future[Seq[UserFormData]] = db.run(Users.result)


  def insert(userForm: UserForm): Future[Int] = user.insert(userForm)

}
