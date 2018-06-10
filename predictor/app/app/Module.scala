package app

import com.google.inject.AbstractModule
import service.ResponseKafkaConsumer

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[ResponseKafkaConsumer]).asEagerSingleton
  }

}
