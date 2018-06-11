package app

import javax.inject.Singleton

import model.{Metrics, Response}

import scala.collection.mutable
import scala.concurrent.Promise

/**
  * Created by Denys Kovalenko on 10/06/18.
  * denis.v.kovalenko@gmail.com
  */
@Singleton
object GlobalState {
  val requestIdPromiseMap = mutable.HashMap[String, Promise[String]]()
  //not really good idea to have var list, but this is prototype (:
  var recentScores = mutable.MutableList.empty[(Long, Response)]
  val emptyMetrics = Metrics(0, Array.empty[String], 0, 0, 0, 0, 0, 0)
  val mostCommonLabelCountMap = scala.collection.mutable.Map[String, Long]()
  var metrics = emptyMetrics
}
