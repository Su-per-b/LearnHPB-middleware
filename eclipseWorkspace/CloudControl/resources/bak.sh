set_bash_profile() {
	printStep 'set PROMPT_COMMAND in .bashrc'
	TMP_FILE="$(mktemp)"

	
	echo $CODE
	exit 0

	echo ''  >> TMP_FILE
	echo "export SERVER=$SERVER_NAME">> ~/.bashrc
	echo '' >> ~/.bashrc

	echo 'case "$TERM" in' >> ~/.bashrc
	echo 'xterm*|rxvt*)' >> ~/.bashrc

	echo '    PROMPT_COMMAND='\''echo -ne "\033]0;${SERVER} : ${PWD}\007"'\' >> ~/.bashrc
	echo '	;;' >> ~/.bashrc
	echo '*)' >> ~/.bashrc

	echo ';;' >> ~/.bashrc
	echo 'esac' >> ~/.bashrc

}
setEnvironmentVariables() {

	printStep 'Set environment variables'
	echo 'JAVA_HOME: ' $JAVA_HOME
	echo 'M2_HOME: ' $M2_HOME

	echo 'export JAVA_HOME=/usr/lib/jvm/jre-1.6.0-openjdk' >> ~/.bashrc
	echo 'export M2_HOME=/usr/local/apache-maven-2.2.1' >> ~/.bashrc
	echo 'export PATH=${M2_HOME}/bin:${PATH}' >> ~/.bashrc
	cat ~/.bashrc

	echo 'JAVA_HOME: ' $JAVA_HOME
	echo 'M2_HOME: ' $M2_HOME
	echo 'mvn -version:'
	mvn -version

}


	BACKED_UP_FILE=$ROOT_HOME/.bashrc.orig	
	FILE=$ROOT_HOME/.bashrc

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