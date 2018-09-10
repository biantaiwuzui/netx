#! /bin/sh

print_log(){
	echo "================================= ${1} ========================================"
}
pid=`ps -auxww|grep index_rebuild|grep -v grep|awk '{print $2}'`
# kill掉
if [ -n "$pid" ] ;then
    print_log "kafka_consumer is starting..."
    print_log "to kill the tomcat pid $pid"
    sudo kill -9 $pid
    sleep 3
fi
tar -zcvf ./logs/index_rebuild.`date -d yesterday +%F`.tar.gz index_rebuild.log
rm index_rebuild.log
nohup python -u index_rebuild.py > index_rebuild.log >&1 &