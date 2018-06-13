package security

import be.objectify.deadbolt.scala.{AuthenticatedRequest, DeadboltHandler}
import be.objectify.deadbolt.scala.models.Subject
import models.User
import play.api.mvc.Request

import scala.concurrent.Future

//class UserAuthorization  extends DeadboltHandler{
//  def beforeAuthCheck[A](request: Request[A]) = Future(None)
//
//  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] = {
//    // e.g. request.session.get("user")
//
//
//    Future(Some(new User("steve")))
//  }
//}
