#!/bin/bash

NETX_BASE=/opt/netx
KAFKA=kafka

if [ ! -d $NETX_BASE/local ]; then
  mkdir -p $NETX_BASE/local
fi

#install docker-compose
curl -L https://github.com/docker/compose/releases/download/1.19.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

#create kafka docker image
docker build -t netx/kafka:latest .


