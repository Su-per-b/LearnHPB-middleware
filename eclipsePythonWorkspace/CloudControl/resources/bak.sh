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



#http://www.gtk.org/download/linux.php
install_gtk() {
	printStep 'Install libffi-3.0.10'
	cd $WORKINGDIR
	wget ftp://sourceware.org/pub/libffi/libffi-3.0.10.tar.gz
	tar xvf libffi-3.0.10.tar.gz
	rm -f libffi-3.0.10.tar.gz
	cd libffi-3.0.10
	./configure --prefix=/usr
	make
	make install

	printStep 'Install GLib 2.30'
	cd $WORKINGDIR
	wget http://ftp.gnome.org/pub/gnome/sources/glib/2.30/glib-2.30.2.tar.bz2
	tar xvfj glib-2.30.2.tar.bz2
	rm -f glib-2.30.2.tar.bz2
	cd glib-2.30.2
	./configure --prefix=/usr
	make
	make install
	export PKG_CONFIG_PATH=/usr/lib/pkgconfig/glib-2.0.pc:${PKG_CONFIG_PATH}

	exit 0
	printStep 'Install ATK 2.0'
	cd $WORKINGDIR
	wget http://ftp.gnome.org/pub/gnome/sources/atk/2.0/atk-2.0.1.tar.bz2
	tar xvfj atk-2.0.1.tar.bz2
	rm -f atk-2.0.1.tar.bz2
	cd atk-2.0.1
	./configure
	make
	make install
	
	

	printStep 'Install Pango 1.28.4'
	cd $WORKINGDIR
	wget http://ftp.gnome.org/pub/gnome/sources/gdk-pixbuf/2.24/gdk-pixbuf-2.24.0.tar.bz2
	tar xvfj gdk-pixbuf-2.24.0.tar.bz2
	rm -f gdk-pixbuf-2.24.0.tar.bz2
	cd gdk-pixbuf-2.24.0
	./configure
	make
	make install



	printStep 'Gdk-Pixbuf 2.24'
	cd $WORKINGDIR
	wget http://ftp.gnome.org/pub/gnome/sources/pango/1.28/pango-1.28.4.tar.bz2
	tar xvfj pango-1.28.4.tar.bz2
	rm -f pango-1.28.4.tar.bz2
	cd pango-1.28.4
	./configure
	make
	make install



	printStep 'Install GTK+-3.2.2'
	cd $WORKINGDIR
	wget http://ftp.gnome.org/pub/gnome/sources/gtk+/3.2/gtk+-3.2.2.tar.bz2
	tar xvfj gtk+-3.2.2.tar.bz2
	rm -f gtk+-3.2.2.tar.bz2
	cd gtk+-3.2.2
	./configure --prefix=/opt/gtk
	make
	make install


	#printStep 'Install GTK+-2.24.8'
	#cd $WORKINGDIR
	#wget http://ftp.gnome.org/pub/gnome/sources/gtk+/2.24/gtk+-2.24.8.tar.bz2
	#tar xvfj gtk+-2.24.8.tar.bz2
	#rm -f gtk+-2.24.8.tar.bz2
	#cd gtk+-2.24.8
	#./configure --prefix=/opt/gtk
	#make
	#make install

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
	
	
	
	#mkdir $HOME/local
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/JPype-0.5.4.2.zip $WORKINGDIR/JPype-0.5.4.2.zip
	
	unzip JPype-0.5.4.2.zip
	rm -f $WORKINGDIR/setup.py
	cp $GIT_REPOSITORY_LOCAL/assets/libs/setup_ubuntu.py $WORKINGDIR/JPype-0.5.4.2/setup.py

	cd JPype-0.5.4.2
	python setup.py install
	