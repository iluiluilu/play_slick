package controllers

import java.io.{File, PrintWriter}
import java.nio.file.Files
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.actor.Status.Success
import controllers.HelloActor.SayHello
import models.UserFormData
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.mvc.{AbstractController, ControllerComponents}
import services.UserService
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
//    actorSystem.scheduler.scheduleOnce(delay = 2.seconds) {
//      // the block of code that will be executed
//      print("Executing something...")
//    }
//    sayHello
//    println("Executing something...")
//
//    helloActor ! SayHello("dfgfgdf")


    var seq = mutable.Set.empty[String]
    for (i <- 0 to 10) {
      seq += i.toString
    }

    val t0 = System.nanoTime()
   /* val writer = new PrintWriter(new File("test.txt" ))
    writer.write(seq.toString().replace("Set(", "").replace(")", "").replace(" ", ""))
    writer.close()*/
    Try {
      Files.write(new File("test.txt" ).toPath, seq.toString().replace("Set(", "").replace(")", "").replace(" ", "").getBytes("UTF-8"))
    }

    val x = Source.fromFile("test.txt" ).mkString.split(",").toSet

    println(x)
    println(x.contains("200"))
    println(x.contains("200000"))
    val t1 = System.nanoTime()
//    println("execute write time: " + (t1 - t0) + "ns")
    Ok(views.html.user())
  }

  def sayHello = Action.async {
    val name: String = "xxxxxxxxxxxxxxxx"
    (helloActor ? SayHello(name)).mapTo[String].map { message => {
      println("hihihi")
      Ok(message)
      }
    }
  }

  val userForm = Form(
    mapping(
      "username" -> text(),
      "password" -> text(),
      "email" -> text(),
    )(UserFormData.apply)(UserFormData.unapply)
  )

  def insert = Action.async { implicit request =>
    val user: UserFormData = userForm.bindFromRequest.get

    actorSystem.scheduler.scheduleOnce(delay = 20.seconds) {
      // the block of code that will be executed
      userService.insert(user)
      println("Executing something...")
    }
//    userService.insert(user).map(_ => Redirect(routes.UserController.index))

    Future(Redirect(routes.UserController.index))
  }


  def testUser = Action {
    val js: JsValue = Json.parse("""
  {
    "x" : {
      "y" : 51.235685,
      "long" : -1.309197
    }
  }
  """)
    (js \ "x" \ "y").asOpt[JsValue].foreach( f => {
      println(f)
//      (f \ "y").asOpt[JsString].foreach( v => {
//        println(v)
//      })
    })
    Ok(views.html.user())
  }
}
