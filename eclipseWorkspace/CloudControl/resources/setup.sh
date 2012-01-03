#!/bin/bash
# run with: sudo ./setup.sh test
set -e #exit script if an error occurs


############################ OPTIONS ######################################################

GIT_REPOSITORY='git://github.com/rajdye/straylight.git'        	# Url for the git repository
SERVER_NAME='StraylightR'

USER_NAME='ec2-user'                                            # User running server
USER_GROUP='ec2-user'
USER_HOME='/home/ec2-user'
LOGFILE='/var/log/straylight.log'                              	# Logfile location and file
ROOT_HOME='/root'

############################ END OPTIONS ##################################################

STEPNUMBER=0

printStep()
{ 
	STEPNUMBER=$[$STEPNUMBER+1]
	echo ''
	echo '*************************************'
	echo '       				   '
	echo '     STEP '$STEPNUMBER' - '$1
	echo '       				   '
	echo '*************************************'
}

info() {
	printStep 'info'
	echo 'whoami:'
	whoami
	python --version
	java -version
	#javac -version

	#yum should be installed
	printStep 'yum --version'
	yum --version
}

precheck() {
	printStep 'precheck'
	USER=$(whoami)
	echo ""
	if [ "$USER" == "root" ]
	then
		echo "You are logged in as:  $USER"	
	else
		echo "You are incorrectly running this script as: $USER"
		usage
	  #echo "$(date +"%b %a %d  %H:%M:%S"): You do not have Sudo installed." >> $LOGFILE
		exit 1
	fi

	echo ""
}

installDependencies() {

	printStep 'add jpackage to yum repos'
	cd /etc/yum.repos.d/
	wget http://jpackage.org/jpackage.repo

	printStep 'update all packages'
	yum -y update

	printStep 'Install locate'
	yum -y install mlocate
	updatedb

	printStep 'Install git'
	yum -y install git

	printStep 'Install JDK'
	yum -y install java-1.6.0-openjdk-devel
	java -version
	javac -version

	printStep 'install maven2'
	cd ~
	wget http://apache.cyberuse.com//maven/binaries/apache-maven-2.2.1-bin.tar.gz
	mv apache-maven-2.2.1-bin.tar.gz /usr/local
	cd /usr/local
	tar -zxvf apache-maven-2.2.1-bin.tar.gz
	rm -f apache-maven-2.2.1-bin.tar.gz
	ln -s apache-maven-2.2.1 maven
	/usr/local/maven/bin/mvn -version
}




cloneGitRepo() {
	printStep 'Clone the git repository'
	cd ~
	git clone $GIT_REPOSITORY straylight_repo

}

mavenBuild() {
	printStep 'Build projects with Maven'
	#checkout the desired build
	cd ~/straylight_repo/eclipseWorkspace/StrayLight
	/usr/local/maven/bin/mvn clean install

	cd ~/straylight_repo/eclipseWorkspace/StrayLight/SocketServer
	/usr/local/maven/bin/mvn clean install

	cd ~/straylight_repo/eclipseWorkspace/StrayLight/PageServer
	/usr/local/maven/bin/mvn clean install

}

copy_binaries() {

	printStep 'Copy Binaries'
	mkdir /usr/local/straylight 
	mkdir /usr/local/straylight/pageserver /usr/local/straylight/pageserver/target
	mkdir /usr/local/straylight/socketserver /usr/local/straylight/socketserver/target

	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/PageServer/target/PageServer-*  /usr/local/straylight/pageserver/target/
	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/PageServer/target/classes  /usr/local/straylight/pageserver/target/
	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/PageServer/.classpath  /usr/local/straylight/pageserver/.classpath
	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/PageServer/pom.xml  /usr/local/straylight/pageserver/pom.xml

	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/SocketServer/target/SocketServer-*  /usr/local/straylight/socketserver/target/
	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/SocketServer/target/classes  /usr/local/straylight/socketserver/target/
	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/SocketServer/.classpath  /usr/local/straylight/socketserver/.classpath
	cp -R $ROOT_HOME/straylight_repo/eclipseWorkspace/StrayLight/SocketServer/pom.xml  /usr/local/straylight/socketserver/pom.xml

	cp $ROOT_HOME/straylight_repo/eclipseWorkspace/CloudControl/resources/straylight.sh $USER_HOME/straylight.sh
	chmod 777 $USER_HOME/straylight.sh
	chown ec2-user:ec2-user $USER_HOME/straylight.sh
}



launch() {
	printStep 'Launch Projects'
	cd /usr/local/straylight/pageserver/
	/usr/local/maven/bin/mvn exec:java -Dexec.mainClass="com.sri.straylight.pageserver.Main" &

	cd /usr/local/straylight/socketserver/
	/usr/local/maven/bin/mvn exec:java -Dexec.mainClass="com.sri.straylight.socketserver.Main" &
}



usage() {
	echo "Usage: sudo ./setup all|clone|env|build|precheck|clean|test"	
}


clean() {
	printStep 'clean'
	rm -Rf ~/straylight_repo
	sudo rm -Rf /usr/local/straylight
	
}

setEnvironmentVars() {
	
	printStep 'setBashPrompt'

	set +e
	read -r -d '' SCRIPT_CODE <<-'EOF'
		export SERVER=StraylightR

		case "$TERM" in
		xterm*|rxvt*)
		    PROMPT_COMMAND='echo -ne "\033]0;${SERVER} : ${PWD}\007"'
			;;
		*)
		;;
		esac

		export JAVA_HOME=/usr/lib/jvm/jre-1.6.0-openjdk
		export M2_HOME=/usr/local/apache-maven-2.2.1
		export PATH=${M2_HOME}/bin:${PATH}
	EOF
	set -e

	BACKED_UP_FILE=$USER_HOME/.bashrc.orig	
	FILE=$USER_HOME/.bashrc

	if [ ! -f $BACKED_UP_FILE ]
	then
		echo '.bashrc.orig not found in '$BACKED_UP_FILE
		echo 'backing up ".bashrc" as ".bashrc.orig'
		
		sudo -u $USER cp $FILE $BACKED_UP_FILE
		sudo -u $USER echo "$SCRIPT_CODE" >> $FILE
		
	else 
		echo '.bashrc.orig found in :'$BACKED_UP_FILE
	fi

	BACKED_UP_FILE=$ROOT_HOME/.bashrc.orig	
	FILE=$ROOT_HOME/.bashrc

	if [ ! -f $BACKED_UP_FILE ]
	then
		echo '.bashrc.orig not found in '$BACKED_UP_FILE
		echo 'backing up ".bashrc" as ".bashrc.orig'
		cp $FILE $BACKED_UP_FILE

		echo "$SCRIPT_CODE" >> $FILE
		
	else 
		echo '.bashrc.orig found in :'$BACKED_UP_FILE
	fi

}

updateLoginScript() {
	BACKED_UP_FILE=$USER_HOME/.bashrc.orig	
	FILE=$USER_HOME/.bashrc

	if [ ! -f $BACKED_UP_FILE ]
	then
		echo '.bashrc.orig not found in '$BACKED_UP_FILE
		echo 'backing up ".bashrc" as ".bashrc.orig'
		cp $FILE $BACKED_UP_FILE

		echo "$SCRIPT_CODE" >> $FILE
		#cat $ROOT_HOME/.bashrc
		
	else 
		echo '.bashrc.orig found in :'$BACKED_UP_FILE
	fi
}

copy_startup_scripts() {

	cp ~/straylight_repo/eclipseWorkspace/CloudControl/resources/straylight.sh $USER_HOME/straylight.sh
	chmod 777 $USER_HOME/straylight.sh
	chown ec2-user:ec2-user $USER_HOME/straylight.sh
	
	cp ~/straylight_repo/eclipseWorkspace/CloudControl/resources/straylight.sh /etc/init.d/straylight.sh
	chmod 777 /etc/init.d/straylight.sh
	
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc0.d/K91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc1.d/K91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc2.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc3.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc4.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc5.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc6.d/K91straylight
	
}


case "$1" in
        'all')
		precheck
		info
		installDependencies
		setEnvironmentVars
		cloneGitRepo
		mavenBuild
		copy_binaries
		copy_startup_scripts
	;;
        'clone')
		precheck
		cloneGitRepo
        ;;
        'env')
		precheck
		setEnvironmentVariables
        ;;
        'build')
		precheck
		mavenBuild
        ;;
        'precheck')
		precheck
        ;;
        'clean')
		precheck
		clean
        ;;
        'test')
		precheck
		info
		setEnvironmentVars
        ;;	
        *)
	usage
esac



exit 0