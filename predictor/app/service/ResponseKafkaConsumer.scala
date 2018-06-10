package service

import java.util.concurrent.Executors
import java.util.{Properties}
import javax.inject.Inject

import app.GlobalState
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import play.api.Configuration
import scala.collection.JavaConverters._

/**
  * Created by Denys Kovalenko on 10/06/18.
  * denis.v.kovalenko@gmail.com
  */
class ResponseKafkaConsumer @Inject()(conf : Configuration) {

  private val kafkaConsumerTopics = conf.get[String]("api.allowed_topics")
    .split(":")
    .map(topic =>{ "response-" + topic})
    .toList.asJavaCollection

  private val brokers = "localhost:9092"
  private val groupId = "ml-predictor-front"
  private val props = createConsumerConfig(brokers, groupId)
  private val consumer = new KafkaConsumer[String, String](props)

  run()

  protected def createConsumerConfig(brokers: String, groupId: String): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  def run() = {

    consumer.subscribe(this.kafkaConsumerTopics)
    println("subscribing to " + this.kafkaConsumerTopics)
    Executors.newSingleThreadExecutor.execute(() => {
      while (true) {
        val records = consumer.poll(10)
        records.forEach(record => {
          try {
            println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset())
            processReceivedScores(record.key(), record.value())
          } catch {
            case e: Exception => println(s"Exception while processing message ${e.getMessage}")
          }
        })
      }
    })
  }

  def processReceivedScores(key : String, response: String): Unit = {
    val requestPromise = GlobalState.requestIdPromiseMap(key)
    requestPromise.success(response)
    GlobalState.requestIdPromiseMap.remove(key)
  }
}