package model

import play.api.libs.json.Json

/**
  * Created by Denys Kovalenko on 10/06/18.
  * denis.v.kovalenko@gmail.com
  */
case class Response(scores : String)

object Response {
  implicit val reads = Json.reads[Response]
  implicit val writes = Json.writes[Response]
}