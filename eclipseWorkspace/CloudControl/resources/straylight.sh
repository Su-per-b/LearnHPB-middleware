# startup script

############################ OPTIONS ######################################################

TITLE='Straylight Server'                          # Name for the server
MAIN_PATH='/usr/local/straylight/'
PAGE_SERVER_SUB_FOLDER='PageServer/target/'
PAGE_SERVER_JAR='PageServer-0.0.3.jar'
PAGE_SERVER_PATH=$MAIN_PATH'pageserver/'

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
  echo "Starting $TITLE"
  echo "$(date +"%b %a %d  %H:%M:%S"): Starting $TITLE" >> $LOGFILE
  cd $PAGE_SERVER_PATH
  /usr/lib/jvm/jre-1.6.0-openjdk/bin/java -classpath /usr/local/apache-maven-2.2.1/boot/classworlds-1.1.jar -Dclassworlds.conf=/usr/local/apache-maven-2.2.1/bin/m2.conf -Dmaven.home=/usr/local/apache-maven-2.2.1 org.codehaus.classworlds.Launcher "exec:java" "-Dexec.mainClass=com.sri.straylight.pageserver.Main"
  exit 1

}


case "$1" in
        'start')
        precheck
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



