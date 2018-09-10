#!/bin/bash
#在当前目录下运行以下命令把外依赖打到本地的maven库中
mvn install:install-file -Dfile=alipay-sdk-java20170829142630.jar -DgroupId=com.alibaba -DartifactId=alipay -Dversion=2017.08.29 -Dpackaging=jar
mvn install:install-file -Dfile=taobao-sdk-java-auto_1455552377940-20160607.jar -DgroupId=com.taobao.auto -DartifactId=taobao-sdk-auto_1455552377940 -Dversion=2016.06.07 -Dpackaging=jar
mvn install:install-file -Dfile=taobao-sdk-java-auto_1455552377940-20160607-source.jar -DgroupId=com.taobao.auto.source -DartifactId=taobao-sdk-auto_source_1455552377940 -Dversion=2016.06.07 -Dpackaging=jar
mvn install:install-file -Dfile=aliyun-java-sdk-core-2.1.7.jar -DgroupId=com.alibaba -DartifactId=aliyun-java-sdk-core -Dversion=2.1.7 -Dpackaging=jar
mvn install:install-file -Dfile=aliyun-java-sdk-sts-2.1.6.jar -DgroupId=com.alibaba -DartifactId=aliyun-java-sdk-sts -Dversion=2.1.6 -Dpackaging=jar
mvn install:install-file -Dfile=aliyun-openservices-1.2.3.jar -DgroupId=com.aliyun -DartifactId=aliyun-openservices -Dversion=1.2.3 -Dpackaging=jar
exit;