WORK_HOME=/usr/local
mkdir $WORK_HOME/canal
CANAL_HOME=$WORK_HOME/canal
#下载canal。develop 1.0.24
wget -P $CANAL_HOME https://github.com/alibaba/canal/releases/download/canal-1.0.24/canal.deployer-1.0.24.tar.gz


tar -xvf $CANAL_HOME/canal.deployer-1.0.24.tar.gz -C $CANAL_HOME
rm $CANAL_HOME/canal.deployer-1.0.24.tar.gz 

sed -i -e 's|canal.instance.master.address = 127.0.0.1:3306|canal.instance.master.address = 39.108.214.90:3306|' $CANAL_HOME/conf/example/instance.properties
sed -i -e 's|canal.instance.master.journal.name = |canal.instance.master.journal.name = mysql-bin.000001|' $CANAL_HOME/conf/example/instance.properties
sed -i -e 's|canal.instance.master.timestamp = |canal.instance.master.timestamp = 1528800494000|' $CANAL_HOME/conf/example/instance.properties
sed -i -e 's|canal.instance.dbUsername = canal|canal.instance.dbUsername = netx|' $CANAL_HOME/conf/example/instance.properties
sed -i -e 's|canal.instance.dbPassword = canal|canal.instance.dbPassword = Pa$$word1234|' $CANAL_HOME/conf/example/instance.properties
sed -i -e 's|canal.instance.defaultDatabaseName =|canal.instance.defaultDatabaseName = netx|' $CANAL_HOME/conf/example/instance.properties

if [-e $/usr/local/canal/bin/canal.pid]
then
	rm /usr/local/canal/bin/canal.pid
fi

if [-e $/usr/local/canal/conf/example/meta.dat]
then
	rm /usr/local/canal/conf/example/meta.dat
fi
#启动canal
sh $CANAL_HOME/bin/start.sh 
