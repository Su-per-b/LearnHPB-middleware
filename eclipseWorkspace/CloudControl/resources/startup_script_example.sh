#!/bin/sh
#Teamspeak 3 NOHUP startup script v0.95
#Written by mancert

###########################################################################################
# Remember to start the teamspeak server manually the first time so you get the
# server admin password and the server admin token.
# If you want to use mysql database, remember to create a ts3db_mysql.ini file
# in the folder ts3 in installed in. And make sure ts3server.ini is set right.
# Changelog @ http://forum.teamspeak.com/showthread.php?t=46989
#
# The test start option will check if you are missing anything before you start the script
#
###########################################################################################

############################ OPTIONS ######################################################
TITLE='Teamspeak 3 server'                          # Name for the server
DAEMON='ts3server_linux_x86'                        # Server binary, here the 32bit
TS3='/home/teamspeak/ts'      # Path to ts3 binary
USER='teamspeak'                                            # User running server
USERG='users'                                          # Usergroup of user
OPT='inifile=ts3server.ini'                                      # inifile=server.inifile
LOGFILE='/var/log/ts3.log'                              # Logfile location and file
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

# Checking for awk
if [ ! -x /usr/bin/awk ]; then
  echo ""
  echo "You do not have Awk installed. Please install it and try again."
  echo "$(date +"%b %a %d  %H:%M:%S"): You do not have awk installed." >> $LOGFILE
  echo ""
  exit 1
fi

# Checking for ts3 binary
if [ ! -x $TS3/$DAEMON ]; then
  echo "Can't find /"
  echo "Is the configs done right? Is  installed?"
  echo "$(date +"%b %a %d  %H:%M:%S"): Can't find /" >> $LOGFILE
  exit 1
fi
echo
echo "All seems fine, try /etc/init.d/ts3 start"
echo
}

service_start() {
TEST=$(ps ax | grep $DAEMON | grep -v export | grep -v grep | wc -l)

# Server not running and no pid-file found
if [ "$TEST" = "0" ] && [ ! -f $TS3/ts3.pid ]; then
  echo
  echo "Starting $TITLE"
  echo "$(date +"%b %a %d  %H:%M:%S"): Starting $TITLE" >> $LOGFILE
  cd $TS3
  LIBPATH=$(pwd)
  su $USER -c "export LD_LIBRARY_PATH=$LIBPATH:$LD_LIBRARY_PATH ; $TS3/$DAEMON $OPT" >> $LOGFILE 2>&1 &
  sleep 1
  sudo -u $USER ps ax | grep -v grep | grep $DAEMON | grep -v export | awk '{print $1}' > $TS3/ts3.pid
  chown $USER:$USERG $TS3/ts3.pid
  echo "$TITLE screen process ID written to $TS3/ts3.pid"
  echo "$TITLE started."
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE started" >> $LOGFILE
  echo
  exit 1
fi

# Server not running and a pid-file is found
if [ "$TEST" = "0" ] && [ -f $TS3/ts3.pid ]; then
  echo
  echo "Server not running but pid-file present"
  echo "Removing pid-file"
  echo "$(date +"%b %a %d  %H:%M:%S"): Server not running but pid-file present" >> $LOGFILE
  echo "$(date +"%b %a %d  %H:%M:%S"): Removing pid-file" >> $LOGFILE
  rm $TS3/ts3.pid
  echo "Old pid file removed"
  echo "$(date +"%b %a %d  %H:%M:%S"): Old pid file removed" >> $LOGFILE
  echo
  echo "Starting $TITLE"
  echo "$(date +"%b %a %d  %H:%M:%S"): Starting $TITLE" >> $LOGFILE
  cd $TS3
  LIBPATH=$(pwd)
  su $USER -c "export LD_LIBRARY_PATH=$LIBPATH:$LD_LIBRARY_PATH ; $TS3/$DAEMON $OPT" >> $LOGFILE 2>&1 &
  sleep 1
  sudo -u $USER ps ax | grep -v grep | grep $DAEMON | grep -v export | grep -v export | awk '{print $1}' > $TS3/ts3.pid
  chown $USER:$USERG $TS3/ts3.pid
  echo "$TITLE screen process ID written to $TS3/ts3.pid"
  echo "$TITLE started."
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE started" >> $LOGFILE
  echo
fi

# Server running and no pid file-found, creates a new one!
if [ "$TEST" = "1" ] && [ ! -f $TS3/ts3.pid ]; then
  echo
  echo "Server is running but no pid file. Creating a new pid file!!"
  echo "$(date +"%b %a %d  %H:%M:%S"): Server is running but no pid file. Creating a new pid file!!" >> $LOGFILE
  sudo -u $USER ps ax | grep -v grep | grep $DAEMON | grep -v export | awk '{print $1'} > $TS3/ts3.pid
  chown $USER:$USERG $TS3/ts3.pid
  echo
  echo "$TITLE is running and new pid-file created"
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE is running and new pid-file created" >> $LOGFILE
  echo
fi

# Server running and pid-file found
if [ "$TEST" = "1" ] && [ -f $TS3/ts3.pid ]; then
  echo
  echo "$TITLE is running!!"
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE is running!!" >> $LOGFILE
  echo
fi
}

service_stop() {
TEST1=$(ps ax | grep -v grep | grep $DAEMON | grep -v export | wc -l)

# Server is not running and no pid-file found
if [ "$TEST1" = "0" ] && [ ! -f $TS3/ts3.pid ]; then
  echo
  echo "$TITLE is not running!!"
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE is not running!!" >> $LOGFILE
  echo
fi

# Server is not running and pid-file found
if [ "$TEST1" = "0" ] && [ -f $TS3/ts3.pid ]; then
  echo
  echo "Server is not running but pid-file is present"
  echo "Removing pid-file"
  echo "$(date +"%b %a %d  %H:%M:%S"): Server is not running but pid-file is present" >> $LOGFILE
  echo "$(date +"%b %a %d  %H:%M:%S"): Removing pid-file" >> $LOGFILE
  rm $TS3/ts3.pid
  echo
  echo "Pid file removed"
  echo "$(date +"%b %a %d  %H:%M:%S"): Pid file removed" >> $LOGFILE
  echo
fi

# Server is running but no pid-file found
if [ "$TEST1" = "1" ] && [ ! -f $TS3/ts3.pid ]; then
  echo
  echo "$TITLE is running but no pid file found."
  echo "Stopping $TITLE"
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE is running but no pid file found." >> $LOGFILE
  echo "$(date +"%b %a %d  %H:%M:%S"): Stopping $TITLE" >> $LOGFILE
  sudo -u $USER ps ax | grep -v grep | grep $DAEMON | grep -v export | awk '{print $1'} > $TS3/$DAEMON.pid
  chown $USER:$USERG $TS3/ts3.pid
  for id in $(cat $TS3/ts3.pid)
  do kill -TERM $id
  echo "Killing process ID $id"
  echo "Removing $TITLE pid file"
  rm -rf $TS3/ts3.pid
  break
  done
  echo "$TITLE stopped"
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE stopped" >> $LOGFILE
  echo
fi

# Server running and pid-file found
if [ "$TEST1" = "1" ] && [ -f $TS3/ts3.pid ]; then
  echo
  echo "Stopping $TITLE"
  echo "$(date +"%b %a %d  %H:%M:%S"): Stopping $TITLE" >> $LOGFILE
  for id in $(cat $TS3/ts3.pid)
  do kill -TERM $id
  echo "Killing process ID $id"
  echo "Removing $TITLE pid file"
  rm -rf $TS3/ts3.pid
  break
  done
  echo "$TITLE stopped"
  echo "$(date +"%b %a %d  %H:%M:%S"): $TITLE stopped" >> $LOGFILE
  echo
fi
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
        echo "Usage ./ts3conf start|stop|restart|precheck"
esac