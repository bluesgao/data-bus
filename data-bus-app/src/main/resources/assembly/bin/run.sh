#!/bin/sh

JAVA_HOME="$JAVA_HOME"

RUNNING_USER=root
if [ -z $APP_HOME ]; then
    APP_HOME=$(cd "../"; pwd)
fi

APP_MAINCLASS=$2
LOGPATH=/wyyt/logs/dubbo/wyyt-data-bus-service

#CLASSPATH=$APP_HOME/conf
for i in "$APP_HOME"/lib/*.jar; do
   if  [[ $i =~ spring ]]; then
       CLASSPATH="$i":"$CLASSPATH"
   else
       CLASSPATH="$CLASSPATH":"$i"
   fi
done

JAVA_OPTS=" -Djava.awt.headless=true "
JAVA_MEM_OPTS=" -server -Xmx4g -Xms4g -Xmn2g -XX:+UseConcMarkSweepGC"

psid=0

checkpid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}


start() {
   checkpid

	if [ ! -d $LOGPATH ];then
	    mkdir -p $LOGPATH
	fi
	
   if [ $psid -ne 0 ]; then
      echo "================================"
      echo "warn: $APP_MAINCLASS already started! (pid=$psid)"
      echo "================================"
   else
      echo -n "Starting $APP_MAINCLASS ..."
      nohup $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS -classpath $CLASSPATH $APP_MAINCLASS 2>&1|/usr/sbin/cronolog $LOGPATH/log.%Y-%m-%d.log &
      checkpid
      if [ $psid -ne 0 ]; then
         echo "(pid=$psid) [OK]"
      else
         echo "[Failed]"
      fi
   fi
}


stop() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP_MAINCLASS ...(pid=$psid) "
      kill -9 $psid
      if [ $? -eq 0 ]; then
         echo "[OK]"
      else
         echo "[Failed]"
      fi

      checkpid
      if [ $psid -ne 0 ]; then
         stop
      fi
   else
      echo "================================"
      echo "warn: $APP_MAINCLASS is not running"
      echo "================================"
   fi
}


status() {
   checkpid

   if [ $psid -ne 0 ];  then
      echo "$APP_MAINCLASS is running! (pid=$psid)"
   else
      echo "$APP_MAINCLASS is not running"
   fi
}


info() {
   echo "System Information:"
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo `$JAVA_HOME/bin/java -version`
   echo
   echo "APP_HOME=$APP_HOME"
   echo "APP_MAINCLASS=$APP_MAINCLASS"
   echo "****************************"
}


case "$1" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
 *)
     echo "Usage: $0 {start|stop|restart|status|info}"
     exit 1
  esac