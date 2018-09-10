#! /bin/sh
### BEGIN INIT INFO
# Provides:          startAll.sh
# Required-Start:    $all
# Required-Stop:
# Default-Start:     0 1 2 3 4 5 6
# Default-Stop:
# Short-Description: Run /data/netx-deploy/startAll.sh if it exist
### END INIT INFO

echo "启动项目中的服务"

echo "启动项目"
cd /data/netx-deploy

echo "启动elasticsearch"
sudo -u cloudzou ./package_and_deploy.sh restart_elasticsearch
sleep 1

sudo -u cloudzou ./package_and_deploy.sh restart_job_tomcat
sleep 1

sudo -u cloudzou ./package_and_deploy.sh deploy_job
sleep 1

sudo -u cloudzou ./package_and_deploy.sh deploy
sleep 7

exit 0
