package security

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import be.objectify.deadbolt.scala.{AuthenticatedRequest, DeadboltHandler, DynamicResourceHandler}
import be.objectify.deadbolt.scala.models.Subject
import models.{User, UserAuth}
import play.api.libs.json.Json
import play.api.mvc.{Request, Result}
import play.api.mvc.Results.Ok

import scala.concurrent.Future

class UserAuthorization(dynamicResourceHandler: Option[DynamicResourceHandler] = None) extends DeadboltHandler {
  def beforeAuthCheck[A](request: Request[A]) = Future(None)

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = {
    Future{None}
  }

  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] = {
    val x = request.session.get("username")
    println(x)

    Future(Some(new UserAuth("linh", List(SecurityRole("admin")), List(SecurityPermission("admin")))))
//    user.getAuthInfo(request.session.get("userId").get.toInt)
  }

  def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = {
    Future {Ok(Json.obj("status" -> "KO", "reason" -> "Auth fail!"))}
  }
}
