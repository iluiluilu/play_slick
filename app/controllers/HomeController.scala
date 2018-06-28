package controllers

import javax.inject._

import be.objectify.deadbolt.scala.DeadboltActions
import models.{LoginForm, User}
import play.api.{Configuration, Logger}
import play.api.http.HttpErrorHandler
import play.api.mvc._
import utils.JS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(deadbolt: DeadboltActions, cc: ControllerComponents, user: User,
  assets: Assets,
  errorHandler: HttpErrorHandler,
  config: Configuration
) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def home: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] = if (resource.startsWith(config.get[String]("apiPrefix"))){
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if (resource.contains(".")) assets.at(resource) else home
  }

//  def index = deadbolt.Restrict(List(Array("admin")))() { authRequest =>
//
////    Logger.info("HelloHello!")
////    val x = authRequest.session
////    println(authRequest.session.get("username"))
////
////    Future {
////      JS.KO("ssdsd")
////    }
//  }

}
