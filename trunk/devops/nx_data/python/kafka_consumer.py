# -* coding:utf8 *-
from pykafka import KafkaClient

hosts = '127.0.0.1:9092'
client = KafkaClient(hosts=hosts)
# 消费者
topic = client.topics['kafka_spark']
consumer = topic.get_simple_consumer(consumer_group='test', auto_commit_enable=True, auto_commit_interval_ms=1,
                                     consumer_id='test')
for message in consumer:
    if message is not None:
        print message.offset, message.value