package app

import com.google.inject.AbstractModule
import service.{ManualStreamingConsumer,  ResponseKafkaConsumer}

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[ResponseKafkaConsumer]).asEagerSingleton()
    bind(classOf[ManualStreamingConsumer]).asEagerSingleton()
  }

}
