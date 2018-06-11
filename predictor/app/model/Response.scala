package model

import play.api.libs.json.Json

/**
  * Created by Denys Kovalenko on 10/06/18.
  * denis.v.kovalenko@gmail.com
  */
case class Response(scores: ToxicCommentScores)

case class ToxicCommentScores(toxic: Double, severe_toxic: Double, obscene: Double, threat: Double, insult: Double, identity_hate: Double)


object ToxicCommentScores {
  implicit val reads = Json.reads[ToxicCommentScores]
  implicit val writes = Json.writes[ToxicCommentScores]
}

object Response {
  implicit val reads = Json.reads[Response]
  implicit val writes = Json.writes[Response]
}
