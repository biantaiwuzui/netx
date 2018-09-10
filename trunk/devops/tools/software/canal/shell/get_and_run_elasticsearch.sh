###############################################################################################################################
WORK_HOME=/usr/local
wget -P $WORK_HOME https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.2.1.tar.gz

tar -xvf $WORK_HOME/elasticsearch-6.2.1.tar.gz -C $WORK_HOME
ES_HOME=$WORK_HOME/elasticsearch-6.2.1

rm $WORK_HOME/elasticsearch-6.2.1.tar.gz

#启动elasticsearch
./$ES_HOME/bin/elasticsearch -d











