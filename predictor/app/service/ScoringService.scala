package service

import java.util.{Properties, UUID}
import javax.inject.{Inject, Singleton}

import app.GlobalState
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import play.api.Configuration

import scala.concurrent.Promise

/**
  * Created by Denys Kovalenko on 10/06/18.
  * denis.v.kovalenko@gmail.com
  */
@Singleton
class ScoringService @Inject()(config: Configuration) {
  val brokers = config.get[String]("kafka.brokers")

  val props = new Properties()
  props.put("bootstrap.servers", brokers)
  props.put("client.id", "RequestProducer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)

  def storePromise(messageId: String, promise: Promise[String]) = {
    GlobalState.requestIdPromiseMap += (messageId -> promise)
  }

  def requestPrediction(project: String, comment: String, promise: Promise[String]) {
    val messageId = UUID.randomUUID().toString
    storePromise(messageId, promise)
    publishToKafka(project, messageId, comment)

  }


  def publishToKafka(project: String, id: String, msg: String): Unit = {
    val data = new ProducerRecord[String, String]("request-" + project, id, msg)
    producer.send(data, (m, e) => {
      e match {
        case null => println(s"successfully sent ${m} with id ${id}")
        case _ => println(s"error while sending to Kafka: ${e.getMessage}")
      }
    })
  }
}
