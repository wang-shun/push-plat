 #!/bin/sh  
    JAVA_HOME=${JAVA_HOME}

    APP_HOME="$(pwd)"
    APP_HOME="$(dirname "${APP_HOME}")"

    APP_MAINCLASS=com.lvmama.bms.scheduler.SchedulerRunner
    #conf
    CLASSPATH=${APP_HOME}/conf

    for i in "${APP_HOME}"/lib/*.jar; do
       CLASSPATH="$CLASSPATH":"$i"  
    done
      
    #java虚拟机启动参数  
    JAVA_OPTS="-ms1024m -mx1024m -Xmn512m -Djava.awt.headless=true -XX:MaxPermSize=256m"  
   
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
       
       if [ $psid -ne 0 ]; then  
          echo "================================"  
          echo "warn: $APP_MAINCLASS already started! (pid=$psid)"  
          echo "================================"  
       else  
          echo -n "Starting $APP_MAINCLASS ..."
          if [ ! -d $APP_HOME/logs ]; then
            mkdir -p  $APP_HOME/logs
          fi

          JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAINCLASS >$APP_HOME/logs/nohup.out 2>&1 &"  
          eval $JAVA_CMD  
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
      
    ###################################  
    #(函数)打印系统环境参数  
    ###################################  
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
      
    ###################################  
    #读取脚本的第一个参数($1)，进行判断  
    #参数取值范围：{start|stop|restart|status|info}  
    #如参数不在指定范围之内，则打印帮助信息  
    ###################################  
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
    exit 0 




