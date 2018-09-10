############################################################################下载java
mkdir /usr/local/java
JAVA_HOME=/usr/local/java

wget -P $JAVA_HOME -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz

tar -xvf $JAVA_HOME/jdk-8u131-linux-x64.tar.gz -C $JAVA_HOME


############################################################################下载maven
mkdir /usr/local/maven
MAVEN_HOME=/usr/local/maven

wget -P $MAVEN_HOME http://ftp.kddilabs.jp/infosystems/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz

tar -xvf $MAVEN_HOME/apache-maven-3.5.4-bin.tar.gz -C $MAVEN_HOME