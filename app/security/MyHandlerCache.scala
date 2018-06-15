package security

import javax.inject.Singleton

import be.objectify.deadbolt.scala.cache.HandlerCache
import be.objectify.deadbolt.scala.{DeadboltHandler, HandlerKey}

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Singleton
class MyHandlerCache extends HandlerCache {

  val defaultHandler: DeadboltHandler = new UserAuthorization

  val handlers: Map[Any, DeadboltHandler] = Map(HandlerKeys.defaultHandler -> defaultHandler,
                                                HandlerKeys.altHandler -> new UserAuthorization(Some(MyAlternativeDynamicResourceHandler)),
                                                HandlerKeys.userlessHandler -> new MyUserlessDeadboltHandler)

  override def apply(): DeadboltHandler = defaultHandler

  override def apply(handlerKey: HandlerKey): DeadboltHandler = handlers(handlerKey)
}
