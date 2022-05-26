#!/bin/sh

echo "##################################"
echo "###      Started               ###"
echo "##################################"

echo ${PATH_LOG_FILE}
echo "The application will start in ${SLEEP}s..." && sleep ${SLEEP}
exec java ${JAVA_OPTS} -jar /home/target/app.jar -Dcom.sun.management.jmxremote $@