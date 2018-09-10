#!/bin/bash
source /etc/profile
tomcat_path="/data/src/trunk/devops/ops/tomcat/job-admin"

print_log(){
	echo "================================= ${1} ========================================"
}
print_log "now is "
date
# 第一步，获取tomcat pid
pid=`ps -auxww|grep tomcat|grep -v grep|awk '{print $2}'`
echo $pid
# kill掉
if [ -n "$pid" ] ;then
    print_log "tomcat is starting..."
    print_log "to kill the tomcat pid $pid"
    kill -9 $pid
    sleep 3
fi

# 第二步清除日志
find /data/logs/tomcat/job-admin  -name "catalina.out" -type f | xargs rm
if [ $? -eq 0 ];then
    print_log "delete catlina.out success!"
else
    print_log "delete catlina.out FAILD!"
fi
sleep 2
find /data/logs/python/ -name "*.log" -type f | xargs rm
if [ $? -eq 0 ];then
    print_log  "delete all dump.logs success!"
else
    print_log  "delete all dump.logs FAILD!"
        fi

# 第三步 重新打开Tomcat
print_log "开启tomcat"
cd ${tomcat_path}/bin
sleep 1
./startup.sh



