WORK_HOME=/usr/local
wget -P $WORK_HOME https://archive.apache.org/dist/kafka/1.1.0/kafka_2.11-1.1.0.tgz

tar -xvf $WORK_HOME/kafka_2.11-1.1.0.tgz -C $WORK_HOME
rm $WORK_HOME/kafka_2.11-1.1.0.tgz
KAFKA_HOME=$WORK_HOME/kafka_2.11-1.1.0

#开启zk
nohup sh $KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties > zookeeper_status.log>&1 &
nohup sh $KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties > kafka_status.log>&1 &