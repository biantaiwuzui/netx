wget http://www-eu.apache.org/dist/spark/spark-2.2.0/spark-2.2.0-bin-hadoop2.7.tgz

tar -zxf spark-2.2.0-bin-hadoop2.7.tgz

cd spark-2.2.0-bin-hadoop2.7

cp conf/spark-defaults.conf.template conf/spark-defaults.conf && cp conf/spark-env.sh.template conf/spark-env.sh


#修改spark-env.sh配置
export MESOS_NATIVE_JAVA_LIBRARY=/usr/local/lib/libmesos.so     #加载Mesos库的路径

export MASTER=mesos://zk://10.0.4.4:2181,10.0.4.5:2181,10.0.4.6:2181/mesos

export SPARK_EXECUTOR_URI=http://www-eu.apache.org/dist/spark/spark-2.2.0/spark-2.2.0-bin-hadoop2.7.tgz