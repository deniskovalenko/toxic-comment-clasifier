package controllers

import javax.inject.{Inject, Singleton}

import app.GlobalState
import play.api.Configuration
import play.api.cache.AsyncCacheApi
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import play.mvc.Results._
import service.ScoringService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

@Singleton
class Application @Inject()(cc: ControllerComponents,
                            scoringService: ScoringService,
                            conf: Configuration,
                            cache: AsyncCacheApi) extends AbstractController(cc) {
  val apiToken = conf.get[String]("api.api_token")
  val allowedTopics = conf.get[String]("api.allowed_topics").split(":")


  def predict(project: String, api_token: String, comment: String) = Action.async {
    //    todo fix returned response if API token is incorrect or project isn't one of allowed
    if (api_token != api_token) {
      Future.successful(unauthorized("api_token is incorrect"))
    } else if (!allowedTopics.contains(project)) {
      Future.successful(badRequest("supplied project " + project + " isn't registered in the system"))
    }

    val promise = Promise[String]
    scoringService.requestPrediction(project, comment, promise)
    //this promise is fullfilled in Kafka Consumer when response from python model is obtained
    promise.future.map(result => Ok(result))
  }

  def metrics(project: String) = Action {
    //    val db = RocksDB.open(options, "/tmp/kafka")
    Ok(Json.obj("metrics" -> Json.toJson(GlobalState.metrics)))
  }

}