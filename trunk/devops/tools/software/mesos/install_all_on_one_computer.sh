#!/usr/bin/env bash
#zookeeper, mesos master, marathon,2个mesos slave 都是部署在同一台服务器上。All node in one computer

#三个镜像, mesos-master/slave都需要
sudo docker pull garland/zookeeper
sudo docker pull garland/mesosphere-docker-mesos-master
sudo docker pull garland/mesosphere-docker-marathon

HOST_IP=10.0.4.4

#master 
sudo docker run -d -p 2181:2181 -p 2888:2888 -p 3888:3888  garland/zookeeper

#master
sudo docker run --net="host" \
-p 5050:5050 \
-e "MESOS_HOSTNAME=${HOST_IP}" \
-e "MESOS_IP=${HOST_IP}" \
-e "MESOS_ZK=zk://${HOST_IP}:2181/mesos" \
-e "MESOS_PORT=5050" \
-e "MESOS_LOG_DIR=/var/log/mesos" \
-e "MESOS_QUORUM=1" \
-e "MESOS_REGISTRY=in_memory" \
-e "MESOS_WORK_DIR=/var/lib/mesos" \
-d \
garland/mesosphere-docker-mesos-master

#master
sudo docker run \
-d \
-p 8080:8080 \
garland/mesosphere-docker-marathon --master zk://${HOST_IP}:2181/mesos --zk zk://${HOST_IP}:2181/marathon


#slave-1
sudo docker run -d \
--name mesos_slave_1 \
--entrypoint="mesos-slave" \
-e "MESOS_MASTER=zk://${HOST_IP}:2181/mesos" \
-e "MESOS_LOG_DIR=/var/log/mesos" \
-e "MESOS_LOGGING_LEVEL=INFO" \
garland/mesosphere-docker-mesos-master:latest

#slave-2
sudo docker run -d \
--name mesos_slave_2 \
--entrypoint="mesos-slave" \
-e "MESOS_MASTER=zk://${HOST_IP}:2181/mesos" \
-e "MESOS_LOG_DIR=/var/log/mesos" \
-e "MESOS_LOGGING_LEVEL=INFO" \
garland/mesosphere-docker-mesos-master:latest