#!/bin/bash

APPDIR='/var/www/webapps/tnt-bms-pusher'

export JAVA_HOME=${JAVA_HOME}
JAVA_OPTS="$JAVA_OPTS -Xmx4096M -Xms4096M -XX:PermSize=128m -XX:MaxPermSize=256m  -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8"

PORT=0
killPort() {
	ps -ef | grep java| grep tnt-bms-pusher | awk '{print $2}' | xargs kill -9
}
echo "JAVA_OPTS $JAVA_OPTS"

killPort
echo "------------------------------"

jarfile="tnt-bms-pusher-exec.jar"
export DIR=$APPDIR

echo "nohup java $JAVA_OPTS -jar $DIR/$jarfile > "$APPDIR/tnt-bms-pusher.out" 2>&1 &"
#nohup java $JAVA_OPTS -jar $APPDIR/$jarfile > "$APPDIR/tnt-bms-pusher.out" 2>&1 &
nohup ${JAVA_HOME}/bin/java $JAVA_OPTS -jar $APPDIR/$jarfile --fangzhen  > "$APPDIR/tnt-bms-pusher.out" 2>&1 &
