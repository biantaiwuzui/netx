#!/usr/bin/env bash
#master
sudo ./sbin/start-master.sh -h 127.0.0.1
#master as slave
sudo ./sbin/start-slave.sh spark://127.0.0.1:7077