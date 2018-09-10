#!/bin/bash
current_path=`pwd`

#source_path=/data/src/trunk/devops/nx_data/nx_source_sync
source_path=/work/sources/netx-server/trunk/devops/nx_data/nx_source_sync
cd ${source_path}
sbt assembly
if [ ! -f ${source_path}/target/scala-2.11/canal-consumer.jar ]; then
    echo "canal-consumer file not exist"
    cd ${current_path}
    exit 0
fi
cp ${source_path}/target/scala-2.11/canal-consumer.jar ${source_path}/src/main/assembly

cd ${current_path}