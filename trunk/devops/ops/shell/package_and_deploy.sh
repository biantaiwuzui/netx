#!/bin/bash
source /etc/profile

project_prefix="netx"
app_src_path="/data/src/trunk"
version="1.0.0-SNAPSHOT"
war_version="1.0.0"

deploy_path="/data/netx-deploy"
es_path="devops/elasticsearch"
es_bin_path="/usr/local/elasticsearch-6.2.1"
boss_web_path="/data/src/trunk/netx-boss-web"

jars=("api" "boss")
job_jars=("schedule")
job_wars=("admin")
java_opts="-Xms1024m -Xmx1024m"
java_2048="-Xms2048m -Xmx2048m"
java_512="-Xms512m -Xmx512m"
java_1024="-Xms1024m -Xmx1024m"
java_3072="-Xms3072m -Xmx3072m"
job_path="/netx-job"
tomcat_path="/data/src/trunk/devops/ops/tomcat/job-admin"
common_jars=("common" "utils" "searchengine" "ucenter" "worth" "credit" "shopping-mall" "fuse")
job_parent_jars=("job")

print_log(){
	echo "================================= ${1} ========================================"
}
print_split_log(){
	echo 
	echo 
	echo 
}

function_force_install_common_jars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/jar.log
	jar_path=${app_src_path}/${jarname}
	cd ${jar_path}
	print_log "更新$jar代码"
	print_log "更新源码路径${jar_path}"
	svn up 

	echo "mvn install ${jar}"
	rm -rf target
	rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
	#mvn清除该APP的target包再重新install
	mvn clean install -Dmaven.test.skip=true

}

function_install_common_jars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/jar.log
	jar_path=${app_src_path}/${jarname}
	cd ${jar_path}
	print_log "更新$jar代码"
	print_log "更新源码路径${jar_path}"
	svn up > $jarlog
	cat $jarlog

	#计数，确认是否有代码更新	
	linenum=0
	while read line
        do
		linenum=$[1+$linenum]
	done < $jarlog

	#如果代码更新，则需进行mvn install
	if [ $linenum -gt 2 ]; then
		echo "mvn install ${jar}"
		rm -rf target
		rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
		#mvn清除该APP的target包再重新install
		mvn clean install -Dmaven.test.skip=true
	fi
}

function_force_install_microservice_jars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/jar.log
	jar_path=${app_src_path}/${jarname}
	cd ${jar_path}
	print_log "更新$jar代码"
	print_log "更新源码路径${jar_path}"
	svn up 

	echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar"
	
	echo "mvn install ${jar}"
	rm -rf target
	rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
	#mvn清除该APP的target包再重新install
	mvn clean install -Dmaven.test.skip=true
	echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar"

	cp -f ~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar ${deploy_path}/
}

function_force_job_install_microservice_jars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/jar.log
	jar_path=${app_src_path}${job_path}/${jarname}
	cd ${jar_path}

	echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar"
	#复制app的java包到部署目录中
	cp -f ~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar ${deploy_path}/
	#删除target文件夹
	rm -rf target
	#删除对应的此APP在源目录的java包
	rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
}

function_force_job_install_microservice_wars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/war.log
	jar_path=${app_src_path}${job_path}/${jarname}
	cd ${jar_path}

	echo "~/.m2/repository/com/netx/${jarname}/${war_version}/${jarname}-${war_version}.war"
	#删除旧的APP的war对应的文件夹
	rm -rf ${tomcat_path}/webapps/${jarname}-${war_version}
	#复制app的war包到tomcat中
	cp -f ~/.m2/repository/com/netx/${jarname}/${war_version}/${jarname}-${war_version}.war ${tomcat_path}/webapps/
	#删除target文件夹
	rm -rf target
	#删除对应的此APP在源目录的java包
	rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
}

function_install_microservice_jars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/jar.log
	jar_path=${app_src_path}/${jarname}
	cd ${jar_path}
	print_log "更新$jar代码"
	print_log "更新源码路径${jar_path}"
	svn up > $jarlog
	cat $jarlog

	#计数，确认是否有代码更新	
	linenum=0
	while read line
        do
		linenum=$[1+$linenum]
	done < $jarlog

	echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar"
	
	#如果代码更新，则需进行mvn install
	if [ $linenum -gt 2 ]; then
		echo "mvn install ${jar}"
		rm -rf target
		rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
		#mvn清除该APP的target包再重新install
		mvn clean install -Dmaven.test.skip=true
		echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar"

		cp -f ~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar ${deploy_path}/
	fi
}

function_install_job_microservice_jars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/jar.log
	jar_path=${app_src_path}${job_path}/${jarname}
	cd ${jar_path}
	print_log "更新$jar代码"
	print_log "更新源码路径${jar_path}"
	svn up > $jarlog
	cat $jarlog

	#计数，确认是否有代码更新	
	linenum=0
	while read line
        do
		linenum=$[1+$linenum]
	done < $jarlog

	echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar"
	
	#如果代码更新，则需进行mvn install
	if [ $linenum -gt 2 ]; then
		echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar"
		#复制该APP的java包到部署目录下
		cp -f ~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${version}.jar ${deploy_path}/
		#删除target文件夹
		rm -rf target
		#删除对应的此APP在源目录的java包
		rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
	fi
}

function_install_job_microservice_wars(){
	jar=$1
    jarname=${project_prefix}-${jar}

	jarlog=/tmp/jar.log
	jar_path=${app_src_path}${job_path}/${jarname}
	cd ${jar_path}
	print_log "更新$jar代码"
	print_log "更新源码路径${jar_path}"
	svn up > $jarlog
	cat $jarlog

	#计数，确认是否有代码更新	
	linenum=0
	while read line
        do
		linenum=$[1+$linenum]
	done < $jarlog

	echo "~/.m2/repository/com/netx/${jarname}/${war_version}/${jarname}-${war_version}.war"
	
	#如果代码更新，则需进行mvn install
	if [ $linenum -gt 2 ]; then
		echo "~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${war_version}.war"
		#删除旧的APP的war对应的文件夹
		rm -rf ${tomcat_path}/webapps/${jarname}-${war_version}
		#复制该APP的java包到tomcat目录下
		cp -f ~/.m2/repository/com/netx/${jarname}/${version}/${jarname}-${war_version}.war ${tomcat_path}/webapps/
		#删除target文件夹
		rm -rf target
		#删除对应的此APP在源目录的java包
		rm -rf ~/.m2/repository/com/netx/${project_prefix}-${jar}
	fi
}

function_war_restart(){
	app="job-admin"
	app_path=${tomcat_path}
	print_log "开始重启$app"
	if [ -d $app_path ]; then
		pid=`ps -ef|grep tomcat|grep "/${app}/bin" |awk '{print $2}'`
		if [ "$pid" != "" ]; then
			sudo kill -9 $pid
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

function_war_start(){
	print_log "开启tomcat"
	cd ${tomcat_path}/bin
	sleep 1
	./startup.sh
}

function_build_boss_web(){
	cd ${boss_web_path}
	svn update
	npm run build
}

function_war_shutdown(){
	app="job-admin"
	app_path=${tomcat_path}
	if [ -d $app_path ]; then
		pid=`ps -ef|grep tomcat|grep "/${app}/bin" |awk '{print $2}'`
		if [ "$pid" != "" ]; then
			sudo kill -9 $pid
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

function_restart(){
	app=$1
	print_log "开始重启$app"

	process_name=${project_prefix}-${app}-1.0.0-SNAPSHOT.jar
	echo ${process_name}

	pid=`ps -ef | grep ${process_name} | grep -v grep | awk '{ print $2 }'`
	echo "进程ID:${pid}"
	if [ "$pid" == "" ]; then
		echo $process_name Application is already stopped
	else
		echo kill -9 $pid
		sudo kill -9 $pid
	fi
	sleep 1

	cd ${deploy_path}
	if [ "$1" == "boss" ];then
		echo "boss"
		nohup java ${java_512} -jar ${process_name} --spring.profiles.active=test > log_${app}.file 2>&1 &
	else
		echo "no boss"
		nohup java ${java_2048} -jar ${process_name} --spring.profiles.active=test > log_${app}.file 2>&1 &
	fi

	print_log "重启$app完成"
	print_split_log
}

function_restart_job(){
	app=$1
	print_log "开始重启$app"

	process_name=${project_prefix}-${app}-1.0.0-SNAPSHOT.jar
	echo ${process_name}

	pid=`ps -ef | grep ${process_name} | grep -v grep | awk '{ print $2 }'`
	echo "进程ID:${pid}"
	if [ "$pid" == "" ]; then
		echo $process_name Application is already stopped
	else
		echo kill -9 $pid
		sudo kill -9 $pid
	fi
	sleep 1

	cd ${deploy_path}
	nohup java ${java_1024} -jar ${process_name} --spring.profiles.active=test > log_${app}.file 2>&1 &

	print_log "重启$app完成"
	print_split_log
}

function_restart_elasticsearch(){
	pid=`ps -ef | grep elasticsearch`
	echo "进程ID:${pid}"
	if [ "$pid" == "" ]; then
		echo elasticsearch Application is already stopped
	else
		echo kill -9 $pid
		sudo kill -9 $pid
	fi
	cd ${es_bin_path}/bin
	export JAVA_HOME=/usr/local/java/jdk1.8.0_141
	export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
	export PATH=$JAVA_HOME/bin:$PATH
	./elasticsearch -d
	sleep 1
	print_log "重启$app完成"
	print_split_log
}

case "$1" in
	deploy )
		for app in "${common_jars[@]}"; do 
			function_install_common_jars $app 
		done 	
		if [ $# -eq 1 ]; then 
			for app in "${jars[@]}"; do 
				function_install_microservice_jars $app 
				function_restart $app
			done 
		else 
			function_install_microservice_jars $2
			function_restart $2
		fi
		;;
	deploy_job )
		for app in "${common_jars[@]}"; do 
			function_install_common_jars $app 
		done 
		for app in "${job_parent_jars[@]}"; do 
			function_install_common_jars $app 
		done 
		if [ $# -eq 1 ]; then 
			for app in "${job_jars[@]}"; do 
				function_install_job_microservice_jars $app
				function_restart_job $app
			done 
		else 
			function_install_job_microservice_jars $2
			function_restart_job $2
		fi
		;;
	deploy_war )
		for app in "${common_jars[@]}"; do 
			function_install_common_jars $app 
		done 
		for app in "${job_parent_jars[@]}"; do 
			function_install_common_jars $app 
		done 
		function_war_shutdown
		if [ $# -eq 1 ]; then 
			for app in "${job_wars[@]}"; do 
				function_install_job_microservice_wars $app
			done 
		else 
			function_install_job_microservice_wars $2
		fi
		function_war_start
		;;
	deploy_force )
		for app in "${common_jars[@]}"; do 
			function_force_install_common_jars $app 
		done
		if [ $# -eq 1 ]; then 
			for app in "${jars[@]}"; do 
				function_force_install_microservice_jars $app 
				function_restart $app
			done 
		else 
			function_force_install_microservice_jars $2
			function_restart $2
		fi
		;;
	deploy_force_job )
		for app in "${common_jars[@]}"; do 
			function_force_install_common_jars $app 
		done 
		for app in "${job_parent_jars[@]}"; do 
			function_force_install_common_jars $app 
		done 
		if [ $# -eq 1 ]; then 
			for app in "${job_jars[@]}"; do 
				function_force_job_install_microservice_jars $app
				function_restart_job $app
			done 
		else 
			function_force_job_install_microservice_jars $2
			function_restart_job $2
		fi
		;;
	deploy_boss_web )
	    function_build_boss_web
	    ;;
	deploy_force_all )
	    for app in "${common_jars[@]}"; do
        	function_force_install_common_jars $app
        done
        for app in "${jars[@]}"; do
        	function_force_install_microservice_jars $app
        	function_restart $app
        done
        for app in "${job_parent_jars[@]}"; do
        	function_force_install_common_jars $app
        done
        for app in "${job_jars[@]}"; do
        	function_force_job_install_microservice_jars $app
        	function_restart_job $app
        done
	    ;;
	deploy_force_war )
		for app in "${common_jars[@]}"; do 
			function_force_install_common_jars $app 
		done 
		for app in "${job_parent_jars[@]}"; do 
			function_force_install_common_jars $app 
		done 
		function_war_shutdown
		if [ $# -eq 1 ]; then 
			for app in "${job_wars[@]}"; do 
				function_force_job_install_microservice_wars $app
			done 
		else 
			function_force_job_install_microservice_wars $2
		fi
		function_war_start
		;;
	deploy_force_all_job )
		for app in "${common_jars[@]}"; do 
			function_force_install_common_jars $app 
		done 
		for app in "${job_parent_jars[@]}"; do 
			function_force_install_common_jars $app 
		done 
		function_war_shutdown
		if [ $# -eq 1 ]; then 
			for app in "${job_wars[@]}"; do 
				function_force_job_install_microservice_wars $app
			done 
			for app in "${job_jars[@]}"; do 
				function_force_job_install_microservice_jars $app
				function_restart_job $app ${java_1024}
			done 
		else 
			function_force_job_install_microservice_wars $2
			function_force_job_install_microservice_jars $3
			function_restart_job $3 ${java_1024}
		fi
		function_war_start
		;;
	deploy_python )
		py_path=${app_src_path}/${es_path}
		cd ${py_path}
		svn up
		print_log "python脚本更新完成"
		;;
	restart_job_tomcat )
	    function_war_restart
	    ;;
	restart_elasticsearch )
		function_restart_elasticsearch
		;;
	*)
	echo "Usage: package_and_deploy.sh deploy"
esac

exit 
