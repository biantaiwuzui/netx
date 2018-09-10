#!/usr/bin/env bash
#1个Master, 2个Slave, 总共安装在3台服务器
#master Ip: 10.0.4.4, , slave-1 ip: 10.0.4.5, slave-2 ip: 10.0.4.6

#每台服务器配置:
HOST_IP_M=10.0.4.4
HOST_IP_S1=10.0.4.5
HOST_IP_S2=10.0.4.6

#Master
sudo docker run -d \
  --net="host" \
  -e SERVER_ID=1 \
  -e ADDITIONAL_ZOOKEEPER_1=server.1=${HOST_IP_M}:2888:3888 \
  -e ADDITIONAL_ZOOKEEPER_2=server.2=${HOST_IP_S1}:2888:3888 \
  -e ADDITIONAL_ZOOKEEPER_3=server.3=${HOST_IP_S2}:2888:3888 \
  garland/zookeeper

#Slave-1
sudo docker run -d \
  --net="host" \
  -e SERVER_ID=2 \
  -e ADDITIONAL_ZOOKEEPER_1=server.1=${HOST_IP_M}:2888:3888 \
  -e ADDITIONAL_ZOOKEEPER_2=server.2=${HOST_IP_S1}:2888:3888 \
  -e ADDITIONAL_ZOOKEEPER_3=server.3=${HOST_IP_S2}:2888:3888 \
  garland/zookeeper

#Slave-2
sudo docker run -d \
  --net="host" \
  -e SERVER_ID=3 \
  -e ADDITIONAL_ZOOKEEPER_1=server.1=${HOST_IP_M}:2888:3888 \
  -e ADDITIONAL_ZOOKEEPER_2=server.2=${HOST_IP_S1}:2888:3888 \
  -e ADDITIONAL_ZOOKEEPER_3=server.3=${HOST_IP_S2}:2888:3888 \
  garland/zookeeper


#Master服务器执行
sudo  docker run \
  -d \
  -p 8080:8080 \
  garland/mesosphere-docker-marathon --master zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/mesos \
  --zk zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/marathon

#Master服务器执行
 sudo docker run --net="host" \
   -p 5050:5050 \
   -e "MESOS_HOSTNAME=${HOST_IP_M}" \
   -e "MESOS_IP=${HOST_IP_M}" \
   -e "MESOS_ZK=zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/mesos" \
   -e "MESOS_PORT=5050" \
   -e "MESOS_LOG_DIR=/var/log/mesos" \
   -e "MESOS_QUORUM=2" \
   -e "MESOS_REGISTRY=in_memory" \
   -e "MESOS_WORK_DIR=/var/lib/mesos" \
   -d \
   garland/mesosphere-docker-mesos-master

sudo docker run -d \
  --entrypoint="mesos-slave" \
  -e "MESOS_MASTER=zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/mesos" \
  -e "MESOS_LOG_DIR=/var/log/mesos" \
  -e "MESOS_LOGGING_LEVEL=INFO" \
  garland/mesosphere-docker-mesos-master:latest


#sudo docker run -d \
#  -p 5051:5051 \
#  --name mesos_slave_1 \
#  --entrypoint="mesos-slave" \
#  -e "MESOS_PORT=5051" \
#  -e "MESOS_MASTER=zk://10.0.4.4:2181/mesos" \
#  -e "MESOS_LOG_DIR=/var/log/mesos" \
#  -e "MESOS_LOGGING_LEVEL=INFO" \
#  garland/mesosphere-docker-mesos-master:latest


#Slave-1服务器执行
sudo docker run --net="host" \
   -p 5050:5050 \
   -e "MESOS_HOSTNAME=${HOST_IP_S1}" \
   -e "MESOS_IP=${HOST_IP_S1}" \
   -e "MESOS_ZK=zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/mesos" \
   -e "MESOS_PORT=5050" \
   -e "MESOS_LOG_DIR=/var/log/mesos" \
   -e "MESOS_QUORUM=2" \
   -e "MESOS_REGISTRY=in_memory" \
   -e "MESOS_WORK_DIR=/var/lib/mesos" \
   -d \
   garland/mesosphere-docker-mesos-master


#Slave-1服务器执行
sudo docker run -d \
  --entrypoint="mesos-slave" \
  -e "MESOS_MASTER=zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/mesos" \
  -e "MESOS_LOG_DIR=/var/log/mesos" \
  -e "MESOS_WORK_DIR=/var/lib/mesos" \
  -e "MESOS_LOGGING_LEVEL=INFO" \
  garland/mesosphere-docker-mesos-master:latest

#sudo docker run -d \
#  --entrypoint="mesos-slave" \
#  -e "MESOS_MASTER=zk://10.0.4.4:2181,10.0.4.5:2181,10.0.4.6:2181/mesos" \
#  -e "MESOS_LOG_DIR=/var/log/mesos" \
#  -e "MESOS_LOGGING_LEVEL=INFO" \
#  garland/mesosphere-docker-mesos-master:latest

#Slave-2服务器执行
sudo docker run --net="host" \
   -p 5050:5050 \
   -e "MESOS_HOSTNAME=${HOST_IP_S2}" \
   -e "MESOS_IP=${HOST_IP_S2}" \
   -e "MESOS_ZK=zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/mesos" \
   -e "MESOS_PORT=5050" \
   -e "MESOS_LOG_DIR=/var/log/mesos" \
   -e "MESOS_QUORUM=2" \
   -e "MESOS_REGISTRY=in_memory" \
   -e "MESOS_WORK_DIR=/var/lib/mesos" \
   -d \
   garland/mesosphere-docker-mesos-master

#Slave-2服务器执行
sudo docker run -d \
  --name="mesos-slave-2" \
  --entrypoint="mesos-slave" \
  -e "MESOS_MASTER=zk://${HOST_IP_M}:2181,${HOST_IP_S1}:2181,${HOST_IP_S2}:2181/mesos" \
  -e "MESOS_LOG_DIR=/var/log/mesos" \
  -e "MESOS_LOGGING_LEVEL=INFO" \
  garland/mesosphere-docker-mesos-master:latest
