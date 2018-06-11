package controllers

import javax.inject.{Inject, Singleton}

import app.GlobalState
import play.api.Configuration
import play.api.cache.AsyncCacheApi
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import play.mvc.Result
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


  def predict(project: String, api_token: String, comment: String)  = Action.async {
    val promise = Promise[String]
    if (apiToken != api_token) {
      promise.success("api_token is incorrect")
      promise.future.map(result => Unauthorized(result))
    } else if (!allowedTopics.contains(project)) {
      val promise = Promise[String]
      promise.success("supplied project " + project + " isn't registered in the system")//Unauthorized("api_token is incorrect"))
      promise.future.map(result => BadRequest(result))
    }
    else {
      val promise = Promise[String]
      scoringService.requestPrediction(project, comment, promise)
      //this promise is fullfilled in Kafka Consumer when response from python model is obtained
      promise.future.map(result => Ok(result))
    }
  }

  def metrics(project: String) = Action {
    Ok(Json.obj("metrics" -> Json.toJson(GlobalState.metrics)))
  }

}