package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class RestApiController @Inject()(cc: ControllerComponents) (implicit executionContext: ExecutionContext) extends AbstractController(cc){
  def getName = Action {
    Ok("Jim")
  }
}
