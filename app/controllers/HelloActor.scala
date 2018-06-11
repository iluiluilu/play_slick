package controllers

import akka.actor.{Actor, Props}
import controllers.HelloActor.SayHello

class HelloActor extends Actor {
  def receive: Receive = {
    case SayHello(name: String) => {
      println("got it")
      sender() ! "Hello, " + name
    }


  }
}

object HelloActor {
  def props = Props[HelloActor]

  case class SayHello(name: String)
}
