#!/usr/bin/env bash
source ~/.bash_profile

#cd /home/user1/demo/
#mv -f jjche-boot-demo.jar  jjche-boot-demo.jar.bak
#cp -rf ./tmp/*.jar ./

ID=`ps -ef | grep jjche-boot-demo | grep -v "grep" | awk '{print $2}'`
echo $ID
for id in $ID
do
kill -9 $id
done

nohup java ${JAVA_OPTS} -jar jjche-boot-demo.jar ${JAVA_WEB_SERVICE_OPTS} &> /dev/null &