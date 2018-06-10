package app

import javax.inject.Singleton

import model.Response

import scala.collection.mutable
import scala.concurrent.Promise
/**
  * Created by Denys Kovalenko on 10/06/18.
  * denis.v.kovalenko@gmail.com
  */
@Singleton
object GlobalState {
  val requestIdPromiseMap = mutable.HashMap[String, Promise[String]]()
}
