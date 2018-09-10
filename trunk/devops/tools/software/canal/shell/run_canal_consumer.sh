rm canal_consume.log
nohup java -jar kafka-canal-1.0-SNAPSHOT.jar >canal_consume.log>&1 &