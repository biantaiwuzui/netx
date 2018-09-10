#创建topic
sh /usr/local/kafka_2.11-1.1.0/bin//kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 5 --topic netx-worth