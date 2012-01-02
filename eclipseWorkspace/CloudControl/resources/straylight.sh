# startup script

############################ OPTIONS ######################################################

TITLE='Straylight Server'                          # Name for the server
MAIN_PATH='/usr/local/straylight'
PAGE_SERVER_PATH=$MAIN_PATH'/pageserver'
PAGE_SERVER_JAR='PageServer-0.0.3.jar'
PAGE_SERVER_MAIN_CLASS='com.sri.straylight.pageserver.Main'
LOGFILE='/var/log/straylight.log'

############################ END OPTIONS ##################################################

########################### NO NEED TO EDIT UNDER HERE ####################################

precheck () {
# Checking for sudo
if [ ! -x /usr/bin/sudo ]; then
  echo ""
  echo "You do not have Sudo installed. Please install it and try again."
  echo "$(date +"%b %a %d  %H:%M:%S"): You do not have Sudo installed." >> $LOGFILE
  echo ""
  exit 1
fi



echo ' Checking for PageServer Jar file: '$PAGE_SERVER_FULL_PATH
# Checking for jar
if [ ! -x $PAGE_SERVER_FULL_PATH ]; then
  echo "Can't find /"
  echo "$(date +"%b %a %d  %H:%M:%S"): Can't find /" >> $LOGFILE
  exit 1
fi
echo
echo "All seems fine, try /etc/init.d/straylight start"
echo
}


service_start() {

  echo
  echo " *** Starting $TITLE ***"
  echo "$(date +"%b %a %d  %H:%M:%S"): Starting $TITLE" >> $LOGFILE
  cd $PAGE_SERVER_PATH
  /usr/lib/jvm/jre-1.6.0-openjdk/bin/java -classpath /usr/local/maven/boot/classworlds-1.1.jar -Dclassworlds.conf=/usr/local/maven/bin/m2.conf -Dmaven.home=/usr/local/apache-maven-2.2.1 org.codehaus.classworlds.Launcher "exec:java" "-Dexec.mainClass=com.sri.straylight.pageserver.Main" &
  
  sleep 1
	
  ps ax | grep -v grep | grep $PAGE_SERVER_MAIN_CLASS | grep -v export | awk '{print $1}' > $PAGE_SERVER_PATH/straylight.pid
  echo "$TITLE screen process ID written to $PAGE_SERVER_PATH/straylight.pid"
  echo "$TITLE started."

  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE started" >> $LOGFILE

 exit 0

}


service_stop() {

  echo "Stopping $TITLE"
  echo "$(date +"%b %a %d  %H:%M:%S"): Stopping $TITLE" >> $LOGFILE
  for id in $(cat $PAGE_SERVER_PATH/straylight.pid)
  do kill -TERM $id
  echo "Killing process ID $id"
  echo "Removing $TITLE pid file"
  rm -rf $PAGE_SERVER_PATH/straylight.pid
  break
  done
  echo "$TITLE stopped"
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE stopped" >> $LOGFILE
  echo

}


case "$1" in
        'start')
        service_start
        ;;
        'stop')
        service_stop
        ;;
        'restart')
        service_stop
        sleep 1
        service_start
        ;;
        'test')
        precheck
        ;;
        *)
        echo "Usage ./straylight start|stop|restart|precheck"
esac



