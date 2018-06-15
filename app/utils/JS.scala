package utils

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{JsObject, JsString, Json, Writes}
import play.api.mvc.Result
import play.api.mvc.Results._

object JS {
  @inline implicit final class JsObjectEx(val u: JsObject) extends AnyVal {
    @inline def OK: Result = Ok(u + ("status", JsString("OK")))
    @inline def KO: Result = Ok(u + ("status", JsString("KO")))
  }
  object OK {
    @inline def apply(fields: (String, JsValueWrapper)*): Result =
      Json.obj(fields: _*).OK

    @inline def result[T](result: T)(implicit w: Writes[T]): Result =
      OK("result" -> result)
  }

  object KO {
    def apply(reason: String, status: Status = Ok): Result =
      status(Json.obj("status" -> "KO", "reason" -> reason))

    @inline
    def reason(reason: String, fields: (String, JsValueWrapper)*): Result =
      (Json.obj(fields: _*) + ("reason" -> JsString(reason))).KO
  }

  /** The message will be fill in client side. @see MsgFail in angular's AppCtrl */
  val GeneralFail = KO("")
  val InvalidData = KO("Dữ liệu không hợp lệ", BadRequest)
  val NotPermit = KO("Bạn không có quyền thực hiện hành động này", Forbidden)
  val NeedLogin = KO("Chưa đăng nhập", Unauthorized)
}
