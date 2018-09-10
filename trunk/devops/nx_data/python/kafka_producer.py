# -* coding:utf8 *-  
import time
import json
import uuid
import random
import threading
from pykafka import KafkaClient

# 创建kafka实例
hosts = '127.0.0.1:9092'
client = KafkaClient(hosts=hosts)

# 打印一下有哪些topic
print client.topics  

# 创建kafka producer句柄
topic = client.topics['kafka_spark']
producer = topic.get_producer()


# work
def work():
    while 1:
        msg = json.dumps({
            "id": str(uuid.uuid4()).replace('-', ''),
            "typ": random.randint(1, 5),
            "profit": random.randint(13, 100)})
        producer.produce(msg)

# 多线程执行
thread_list = [threading.Thread(target=work) for i in range(10)]
for thread in thread_list:
    thread.setDaemon(True)
    thread.start()

time.sleep(60)

# 关闭句柄, 退出
producer.stop()