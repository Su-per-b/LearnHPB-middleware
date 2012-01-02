# startup script
set -e


LOGFILE='/var/log/straylight.log'
MAIN_PATH='/usr/local/straylight'

DEBUG=false





precheck () {
output "***precheck requested***"

output 'check for sudo' debug
if [ ! -x /usr/bin/sudo ]; then
  output "You do not have Sudo installed. Please install it and try again."
  exit 1
fi

output 'check for awk' debug
if [ ! -x /usr/bin/awk ]; then
  output ""
  output "You do not have Awk installed. Please install it and try again."
  output "$(date +"%b %a %d  %H:%M:%S"): You do not have awk installed." >> $LOGFILE
  output ""
  exit 1
fi

output "All seems fine, try ./straylight.sh start"
output ''
}

checkForProcess() {
	IS_PROCESS_RUNNING=$(ps ax | grep -v grep | grep $CLASS | grep -v export | wc -l)
	
	if [ "$IS_PROCESS_RUNNING" == "0" ]; then
		IS_PROCESS=false
	else 
		if [ "$IS_PROCESS_RUNNING" == "1" ]; then
			IS_PROCESS=true
		else
			output "Error checking for running process - returned:  $IS_PROCESS_RUNNING"
			exit 1
		fi	
	fi

	#output 'IS_PROCESS_RUNNING='$IS_PROCESS_RUNNING  debug
}


checkForPid() {
	if [ -f $PID_FILE ]; then
		IS_PID_FILE=true
	else
		IS_PID_FILE=false
	fi
}


setvars() {

	if [ "$1" == "pageserver" ]; then
		TITLE='Straylight PageServer'
		CLASS='com.sri.straylight.pageserver.Main'
		PID_FILE=$MAIN_PATH/pageserver.pid
		SERVER_PATH=$MAIN_PATH/pageserver
	else 
		if [ "$1" == "socketserver" ]; then
			TITLE='Straylight SocketServer'
			CLASS='com.sri.straylight.socketserver.Main'
			PID_FILE=$MAIN_PATH/socketserver.pid
			SERVER_PATH=$MAIN_PATH/socketserver
		else
			output "service_start must be called with an argument of pageserver or socketserver"
			exit 1
		fi
	fi
}

service_status() {
	setvars $1
	output "***$TITLE status requested***"

	checkForProcess
	output "IS_PROCESS=$IS_PROCESS"

	checkForPid
	output "IS_PID_FILE=$IS_PID_FILE"

	# Server not running and no pid-file found
	if ! $IS_PROCESS && ! $IS_PID_FILE; then
	  output "$TITLE process was not found and no pid-file found" debug	
	fi

	# Server is not running and pid-file found
	if ! $IS_PROCESS && $IS_PID_FILE; then
	  output "$TITLE process was not found but pid-file is present"
	fi

	# Server is running but no pid-file found
	if $IS_PROCESS && ! $IS_PID_FILE; then
	  output "$TITLE process was found but no pid file found."
	fi

	# Server running and pid-file found
	if $IS_PROCESS && $IS_PID_FILE; then
	  output "$TITLE process was found and pid-file found."
	fi

}


service_start() {

	setvars $1
	output "***$TITLE start requested***"

	checkForProcess
	output "IS_PROCESS=$IS_PROCESS" debug

	checkForPid
	output "IS_PID_FILE=$IS_PID_FILE" debug

	# Server not running and no pid-file found
	if ! $IS_PROCESS && ! $IS_PID_FILE; then
	  output "$TITLE process was not found and no pid-file found" debug
	  service_start_helper "$TITLE" $SERVER_PATH $CLASS	
	fi

	# Server is not running and pid-file found
	if ! $IS_PROCESS && $IS_PID_FILE; then
	  output "$TITLE process was not found but pid-file is present"
	  output "Removing pid-file $PID_FILE "
	  rm $PID_FILE
	  output "Pid file removed please rerun script"
	  return 1
	fi

	# Server is running but no pid-file found
	if $IS_PROCESS && ! $IS_PID_FILE; then
	  output "$TITLE process was found but no pid file found."
	  writePidFile
	  output "Pid file written"
	  return 1
	fi

	# Server running and pid-file found
	if $IS_PROCESS && $IS_PID_FILE; then
	  output "$TITLE process was found and pid-file found."
	  output "You cannot start the server it is alraedy running"
	  return 1
	fi

}


service_start_helper() {
	TITLE=$1
	WORKER_DIR=$2
	CLASS=$3

	output " *** Starting $TITLE ***"
	cd "$WORKER_DIR"
	
	/usr/lib/jvm/jre-1.6.0-openjdk/bin/java -classpath /usr/local/maven/boot/classworlds-1.1.jar -Dclassworlds.conf=/usr/local/maven/bin/m2.conf -Dmaven.home=/usr/local/apache-maven-2.2.1 org.codehaus.classworlds.Launcher "exec:java" "-Dexec.mainClass=$CLASS" &
	sleep 1

	writePidFile
	output "$TITLE started."
}


service_stop() {
	setvars $1
	output 'service_stop()' debug
	output "***$TITLE stop requested***"
	output 'PID_FILE='$PID_FILE debug

	checkForProcess
	output "IS_PROCESS=$IS_PROCESS" debug

	checkForPid
	output "IS_PID_FILE=$IS_PID_FILE" debug

	# process was not found and no pid-file found
	if ! $IS_PROCESS && ! $IS_PID_FILE; then
	  output "$TITLE process was not found and no pid-file found"
	fi

	# Server is not running and pid-file found
	if ! $IS_PROCESS && $IS_PID_FILE; then
	  output "$TITLE process was not found but pid-file is present"
	  output "Removing pid-file $PID_FILE"
	  rm $PID_FILE
	  output "Pid file removed"
	fi

	# Server is running but no pid-file found
	if $IS_PROCESS && ! $IS_PID_FILE; then
	  output "$TITLE process was found but no pid file found."
	  writePidFile
	  killProcess
	fi

	# Server running and pid-file found
	if $IS_PROCESS && $IS_PID_FILE; then
	  output "$TITLE process was found and pid-file found."
	  killProcess
	fi
}



writePidFile() {
  output 'writePidFile()' debug
  ps ax | grep -v grep | grep $CLASS | grep -v export | awk '{print $1}' > $PID_FILE
  output "$TITLE screen process ID written to $PID_FILE"
  cat "$PID_FILE"
}


killProcess() {
	output 'killProcess()' debug
	output "Stopping $TITLE"
	output 'PID_FILE='$PID_FILE debug

	cat $PID_FILE

	for id in $(cat $PID_FILE)
	do 
	  output "Killing process ID $id"
	  kill -TERM $id
	  output "Removing $TITLE pid file"
	  break
	done

	rm -rf $PID_FILE
	output "$TITLE stopped"
}

clean() {
	rm -f $LOGFILE
}


output() {
	MESSAGE=$1
	DEBUG_MESSAGE=$2
	
	#echo 'DEBUG_MESSAGE='$DEBUG_MESSAGE

	if [ "$DEBUG_MESSAGE" == "debug" ] && ! $DEBUG; then
		#echo "ignore: $1"
		return
	else
		echo "$MESSAGE"
		echo "$(date +"%b %a %d  %H:%M:%S"): $MESSAGE" >> $LOGFILE
	fi
}


case "$1" in
        'start')
	        service_start 'pageserver'
		service_start 'socketserver'
        ;;
        'stop')
	        service_stop 'pageserver'
		service_stop 'socketserver'
        ;;
        'clean')
        	clean
        ;;
        'restart')
	        service_stop 'pageserver'
		service_stop 'socketserver'
	        sleep 1
	        service_start 'pageserver'
		service_start 'socketserver'
        ;;
        'precheck')
         	precheck
        ;;
        'status')
	        service_status 'pageserver'
		service_status 'socketserver'
        ;;
        *)
        echo "Usage ./straylight start|stop|restart|precheck|status"
esac



