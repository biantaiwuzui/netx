#!/bin/bash
source /etc/profile

tomcat_dir="/data/src/trunk/devops/ops/tomcat"
op_path="/data/src/trunk/devops/ops"
log_path_base="/data/logs"
user=$USER
print_log(){
	echo "================================= ${1} ========================================"
}
function_install(){
	app=$1
	print_log "开始安装${app}的tomcat"

	#tomcat_name="apache-tomcat-8.0.24"
	tomcat_name="apache-tomcat-7.0.86"
	tomcat_src_path="${op_path}/tools/${tomcat_name}.tar.gz"
	app_tomcat_path=$tomcat_dir/$app
	server_xml=server.xml
	tar -zxvf ${tomcat_src_path} -C ${tomcat_dir}
	if [ -d ${app_tomcat_path} ];then
		rm -rf ${app_tomcat_path}
	fi
	#更改目录名
	mv ${tomcat_dir}/${tomcat_name} ${app_tomcat_path}
	#sudo chown -R $USER:$USER ${app_tommcat_path}
	#更改conf目录下的server.xml，重定向到指定的文件
	cd ${app_tomcat_path}/conf
	rm -rf ${server_xml}
	cp -r ${op_path}/shell/server.xml server.xml
	#ln -s ${op_path}/tomcat/${app}/$server_xml $server_xml

	#更改bin目录下的catalina.sh
	CATALINASH=catalina.sh
	cd ${app_tomcat_path}/bin
	mv $CATALINASH ${CATALINASH}_bak
	cp ${op_path}/shell/${CATALINASH} .
	chmod +x ./${CATALINASH} 

	#更改Tomcat的logs目录
	log_tomcat_app=${log_path_base}/tomcat/$app
	mkdir -p ${log_tomcat_app}
	chmod 777 ${log_tomcat_app}
	#sudo chown -R $USER:$USER ${log_tomcat_app}

    cp -R ${app_tomcat_path}/logs/. ${log_tomcat_app}/logs
    rm -rf ${app_tomcat_path}/logs
	ln -s ${log_tomcat_app} ${app_tomcat_path}/logs

	
	print_log "安装${app}应用系统的tomcat已完成"
}
function_restart(){
	app=$1
	app_path=${tomcat_dir}/$app
	print_log "开始重启$app"
	if [ -d $app_path ]; then
		pid=`ps -ef|grep tomcat|grep "/${app}/bin" |awk '{print $2}'`
		if [ "$pid" != "" ]; then
			kill -9 $pid
		fi
		
		cd $app_path
		#./bin/shutdown.sh
		sleep 1
		./bin/startup.sh
		
		sleep 7
		print_log "重启$app完成"
	else
		echo "$app_path不存在，无法重启"
		exit 1
	fi
}

case "$1" in
	install )
		function_install $2
		;;
	restart )
		function_restart $2
		;;
	*)
	echo "Usage: tomcat.sh install"
esac

exit 
