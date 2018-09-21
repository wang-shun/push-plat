#!/bin/bash

APPDIR='/var/www/webapps/tnt-bms-admin'

export JAVA_HOME="$JAVA_HOME}"
#export JAVA_OPTS="$JAVA_OPTS -Xmx4096M -Xms4096M -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8"
JAVA_OPTS="$JAVA_OPTS -Xmx4096M -Xms4096M -XX:PermSize=128m -XX:MaxPermSize=256m  -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8"

PORT=0
killPort() {
	ps -ef | grep java| grep tnt-cop-scenic-product | awk '{print $2}' | xargs kill -9
}

echo "JAVA_OPTS $JAVA_OPTS"

killPort
echo "------------------------------"

jarfile="tnt-bms-admin-exec.jar"

export DIR=$APPDIR
echo $APPDIR/$jarfile

echo "nohup java $JAVA_OPTS -jar $DIR/$jarfile > "$APPDIR/tnt-bms-admin.out" 2>&1 &"
#nohup java $JAVA_OPTS -jar $APPDIR/$jarfile > "$APPDIR/tnt-bms-admin.out" 2>&1 &
nohup /usr/java/jdk1.8.0_112/bin/java $JAVA_OPTS -jar $APPDIR/$jarfile > "$APPDIR/tnt-bms-admin.out" 2>&1 &
