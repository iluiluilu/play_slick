import com.google.inject.AbstractModule
import controllers.HelloActor
import play.api.{Configuration, Environment}
import play.api.libs.concurrent.AkkaGuiceSupport

class PlayModule(env: Environment, cfg: Configuration) extends AbstractModule with AkkaGuiceSupport {
  def configure(): Unit = {
    bindActor[HelloActor]("HelloActor")
  }
}
