import threading, logging, time
import multiprocessing
from PredictionService import PredictionService
from kafka import KafkaConsumer, KafkaProducer
import json

class Producer():
    def __init__(self, topic):
        self.kafka_producer = KafkaProducer(bootstrap_servers='localhost:9092', value_serializer=lambda v: json.dumps(v).encode('utf-8'))
        self.topic = topic

    def send(self, msg, key):
        self.kafka_producer.send(self.topic, key=key, value=msg)

class Consumer(multiprocessing.Process):
    def __init__(self, prediction_service, project, project_topic):
        multiprocessing.Process.__init__(self)
        self.stop_event = multiprocessing.Event()
        self.prediction_service = prediction_service
        self.project_topic = project_topic
        self.project = project

    def stop(self):
        self.stop_event.set()

    def run(self):
        consumer = KafkaConsumer(bootstrap_servers='localhost:9092',
                                 auto_offset_reset='earliest',
                                 consumer_timeout_ms=100,
                                 group_id='toxic-comments-classifier-1'
                                 )
        self.producer = Producer('response-toxic-comments')
        consumer.subscribe([self.project_topic])

        while not self.stop_event.is_set():
            for message in consumer:
                #takes around 1.4 seconds... Batching?
                result = self.prediction_service.predict(self.project, message.value)
                print('sending ', result)
                self.producer.send(result, message.key)
                if self.stop_event.is_set():
                    break

        consumer.close()

def main():
    prediction_service = PredictionService()
    consumer = Consumer(prediction_service, 'toxic-comments', 'request-toxic-comments')
    consumer.start()

if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO
    )
    main()