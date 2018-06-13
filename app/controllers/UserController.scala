package controllers

import java.io.{File, PrintWriter}
import java.nio.file.Files
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.actor.Status.Success
import controllers.HelloActor.SayHello
import models.UserForm
import services.UserService
//import models.UserFormData
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.mvc.{AbstractController, ControllerComponents}
//import services.UserService
import akka.pattern.ask
import akka.util.Timeout
import play.api.libs.json.{JsString, JsValue, Json}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.Try


@Singleton
class UserController  @Inject() (actorSystem: ActorSystem, userService: UserService, cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  val helloActor = actorSystem.actorOf(HelloActor.props, "hello-actor")

  implicit val timeout: Timeout = 5.seconds

  def index = Action {
    Ok(views.html.user())
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

    x.onComplete{
      rs => {
        println(rs)
      }
    }


    Future(Redirect(routes.UserController.index))
  }
}
