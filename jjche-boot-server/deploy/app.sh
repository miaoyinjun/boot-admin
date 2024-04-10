#!/usr/bin/env bash
APP_NAME=jjche-boot-server.jar
JVM_OPTS="-Xms4096m -Xmx4096m -XX:NewSize=2250M -XX:MaxNewSize=2250M -XX:MetaspaceSize=256m
          -XX:MaxMetaspaceSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:+ExitOnOutOfMemoryError
          -XX:HeapDumpPath=./ -Dfile.encoding=utf-8 "

APP_OPTS="-DJJCHE_DB_HOST=localhost -DJJCHE_DB_PORT=3306 -DJJCHE_DB_UNAME=root
           -DJJCHE_DB_PASSWORD=123 -DJJCHE_REDIS_HOST=localhost -DJJCHE_REDIS_PORT=6379
           -DJJCHE_REDIS_PASSWORD=123 "

SPRING_OPTS="--spring.profiles.active=test"


#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh app.sh [start|stop|restart|status]"
    exit 1
}

#检查程序是否在运行
is_exist() {
    pid=`ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}' `
    #如果不存在返回1，存在返回0
    if [ -z "${pid}" ]; then
      return 1
    else
      return 0
    fi
}
#启动方法
start() {
   is_exist
   if [ $? -eq "0" ]; then
     echo "${APP_NAME} is already running. pid=${pid} ."
   else
     nohup java -jar ${JVM_OPTS} ${APP_OPTS} $APP_NAME ${SPRING_OPTS} > /dev/null 2>&1 &
   fi
}
#停止方法
stop() {
   is_exist
   if [ $? -eq "0" ]; then
     kill -9 $pid
   else
     echo "${APP_NAME} is not running"
   fi
}
#输出运行状态
status() {
   is_exist
   if [ $? -eq "0" ]; then
     echo "${APP_NAME} is running. Pid is ${pid}"
   else
     echo "${APP_NAME} is not running."
   fi
}
#重启
restart() {
   stop
   start
}
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
   "start")
     start
     ;;
   "stop")
     stop
     ;;
   "status")
     status
     ;;
   "restart")
     restart
     ;;
   *)
     usage
     ;;
esac
