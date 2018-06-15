package modules

import be.objectify.deadbolt.scala.cache.HandlerCache
import play.api.Environment
import play.api.Configuration
import play.api.inject.{Binding, Module}
import security.MyHandlerCache

/**
 * A custom execution context can be provided to Deadbolt for asynchronous operations.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
class CustomDeadboltHook extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[HandlerCache].to[MyHandlerCache]
  )
}
