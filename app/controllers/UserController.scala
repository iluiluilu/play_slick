package controllers
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import be.objectify.deadbolt.scala.DeadboltActions
import models.{LoginForm, UserForm}
import services.UserService
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController  @Inject() (deadbolt: DeadboltActions, actorSystem: ActorSystem, userService: UserService, cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  def login = Action.async(parse.json[LoginForm]) { req =>
    userService.login(req.body)
  }


  def index = deadbolt.Restrict(List(Array("admin")))() { authRequest =>
    val x = authRequest.session
      Future(Ok(views.html.user()))
  }

  val userForm = Form(
    mapping(
      "username" -> text(),
      "password" -> text(),
      "email" -> text(),
    )(UserForm.apply)(UserForm.unapply)
  )

  def insert = Action.async { implicit request =>
    val user: UserForm = userForm.bindFromRequest.get
    val x = userService.insert(user)

    x.onComplete {
      rs => {
        println(rs)
      }
    }


    Future(Redirect(routes.UserController.index))
  }
}
