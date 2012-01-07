#!/bin/bash
# run with: sudo ./setup.sh test
set -e #exit script if an error occurs

# version StrayLight2
#installDependencies
#setEnvironmentVars
#cloneGitRepo
#install_python

# version StrayLight3
#install_ipopt

############################ OPTIONS ######################################################

GIT_REPOSITORY_REMOTE='git://github.com/rajdye/straylight.git'        	# Url for the git repository
SERVER_NAME='StraylightR'

USER_NAME='ec2-user'                                            # User running server
USER_GROUP='ec2-user'
USER_HOME='/home/ec2-user'
LOGFILE='/var/log/straylight.log'                              	# Logfile location and file
ROOT_HOME='/root'
PAUSE=true 							#pause before executing each step for debugging
WORKINGDIR='/var/tmp'
GIT_REPOSITORY_LOCAL="$WORKINGDIR/straylight_repo"
INSTALL_DIR='/usr/local/straylight'

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

	if $PAUSE; then
		read -p "Press any key to continue"
	fi

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
	
	printStep 'install ant'
	yum -y install ant

	printStep 'install maven2'
	cd $WORKINGDIR
	wget http://apache.cyberuse.com//maven/binaries/apache-maven-2.2.1-bin.tar.gz
	mv apache-maven-2.2.1-bin.tar.gz /usr/local
	cd /usr/local
	tar -zxvf apache-maven-2.2.1-bin.tar.gz
	rm -f apache-maven-2.2.1-bin.tar.gz
	ln -s apache-maven-2.2.1 maven
	/usr/local/maven/bin/mvn -version

	printStep 'Development Tools'
	cd $WORKINGDIR
	yum -y groupinstall 'Development Tools'
	yum -y install openssl-devel* zlib*.x86_64
}




cloneGitRepo() {
	printStep 'Clone the git repository'
	#cd $WORKINGDIR
	git clone $GIT_REPOSITORY_REMOTE $GIT_REPOSITORY_LOCAL
}


updateGitRepo() {
	printStep 'Update the git repository'
	cd $GIT_REPOSITORY_LOCAL
	git pull
}


# go to the srouce code folder and build binaries with Maven
mavenBuild() {
	printStep 'Build projects with Maven'
	#checkout the desired build
	cd $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight
	/usr/local/maven/bin/mvn clean install

	cd $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/SocketServer
	/usr/local/maven/bin/mvn clean install

	cd $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/PageServer
	/usr/local/maven/bin/mvn clean install

}

mkDirs() {
	printStep 'Make Directories'
	mkdir $INSTALL_DIR 
	mkdir $INSTALL_DIR/pageserver $INSTALL_DIR/pageserver/target
	mkdir $INSTALL_DIR/socketserver $INSTALL_DIR/socketserver/target
}


copy_binaries() {
	printStep 'Copy Binaries'

	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/PageServer/target/PageServer-*  $INSTALL_DIR/pageserver/target/
	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/PageServer/target/classes  $INSTALL_DIR/pageserver/target/
	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/PageServer/.classpath  $INSTALL_DIR/pageserver/.classpath
	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/PageServer/pom.xml  $INSTALL_DIR/pageserver/pom.xml

	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/SocketServer/target/SocketServer-*  $INSTALL_DIR/socketserver/target/
	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/SocketServer/target/classes  $INSTALL_DIR/socketserver/target/
	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/SocketServer/.classpath  $INSTALL_DIR/socketserver/.classpath
	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/SocketServer/pom.xml  $INSTALL_DIR/socketserver/pom.xml

	cp $GIT_REPOSITORY_LOCAL/eclipseWorkspace/CloudControl/resources/straylight.sh $USER_HOME/straylight.sh
	chmod 777 $USER_HOME/straylight.sh
	chown ec2-user:ec2-user $USER_HOME/straylight.sh
}



launch() {
	printStep 'Launch Projects'
	cd $INSTALL_DIR/pageserver/
	/usr/local/maven/bin/mvn exec:java -Dexec.mainClass="com.sri.straylight.pageserver.Main" &

	cd $INSTALL_DIR/socketserver/
	/usr/local/maven/bin/mvn exec:java -Dexec.mainClass="com.sri.straylight.socketserver.Main" &
}



usage() {
	echo "Usage: sudo ./setup all|clone|env|build|precheck|clean|test|update"	
}

#clean before upgrade
clean() {
	printStep 'clean'
	
	#remove all binaries
	rm -Rf $INSTALL_DIR
	
	#remove scripts
	rm -f /etc/init.d/straylight.sh
	rm -f $USER_HOME/straylight.sh
	rm -f $USER_HOME/setup.sh

}

#after clean, then remove everything
uninstall() {
	rm -Rf $GIT_REPOSITORY_LOCAL

	rm -f /etc/init.d/straylight.sh /etc/rc.d/rc0.d/K91straylight
	rm -f /etc/init.d/straylight.sh /etc/rc.d/rc1.d/K91straylight
	rm -f /etc/init.d/straylight.sh /etc/rc.d/rc2.d/S91straylight
	rm -f /etc/init.d/straylight.sh /etc/rc.d/rc3.d/S91straylight
	rm -f /etc/init.d/straylight.sh /etc/rc.d/rc4.d/S91straylight
	rm -f /etc/init.d/straylight.sh /etc/rc.d/rc5.d/S91straylight
	rm -f /etc/init.d/straylight.sh /etc/rc.d/rc6.d/K91straylight

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
		export PYTHON_HOME=/opt/python2.7.2
		export PATH=${M2_HOME}/bin:${PYTHON_HOME}/bin:${PATH}

		alias python='${PYTHON_HOME}/bin/python'
		alias python2.7='${PYTHON_HOME}/bin/python'

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
	
}


#install Python
# from http://mythinkpond.wordpress.com/2011/12/28/how-to-upgrade-to-python-2-7-on-centos/
# http://willsani.com/2011/03/02/centos-5-5-x86_64-install-python-2-7/
install_python() {

	printStep 'Install SQL Lite'
	cd $WORKINGDIR
	wget http://sqlite.org/sqlite-amalgamation-3.7.3.tar.gz
	tar xfz sqlite-amalgamation-3.7.3.tar.gz
	rm -f ./sqlite-amalgamation-3.7.3.tar.gz
	cd sqlite-3.7.3/
	./configure
	make
	make install
	
	
	printStep 'Install Python-2.7.2'
	cd $WORKINGDIR
	wget http://python.org/ftp/python/2.7.2/Python-2.7.2.tgz
	tar xfz Python-2.7.2.tgz
	rm -f Python-2.7.2.tgz
	cd Python-2.7.2
	./configure --prefix=/opt/python2.7.2 --with-threads --enable-shared
	make
	make install

	touch /etc/ld.so.conf.d/opt-python2.7.2.conf
	echo "/opt/python2.7.2/lib/" >> /etc/ld.so.conf.d/opt-python2.7.2.conf

	ln -sf /opt/python2.7.2/bin/python /usr/bin/python2.7
	#ln -sf /opt/python2.7.2/bin/python /usr/bin/python
	#ln -sf /usr/lib/python2.6 /usr/bin/python
	
	ln -sf /opt/python2.7.2/lib/libpython2.7.so /usr/lib/libpython2.7.so

	ldconfig

	printStep 'Install Python Setup Tools'
	cd $WORKINGDIR
	wget http://pypi.python.org/packages/2.7/s/setuptools/setuptools-0.6c11-py2.7.egg
	chmod 775 setuptools-0.6c11-py2.7.egg
	sh setuptools-0.6c11-py2.7.egg --prefix=/opt/python2.7.2
	#This should install the egg here: /opt/python2.7.2/site-packages/

	/opt/python2.7.2/bin/easy_install pip
	ln -sf /opt/python2.7.2/bin/pip /usr/bin/pip

	pip install virtualenv
	ln -sf /opt/python2.7.2/bin/virtualenv /usr/bin/virtualenv


}


# JPype 0.5.4.2 (http://jpype.sourceforge.net/) 
	#http://hep.phys.utk.edu/BRM_Interface/index.php/Installing_JPype
# lxml 2.3 (http://codespeak.net/lxml/) 
# NumPy 1.6.1 (http://numpy.scipy.org/) http://www.scipy.org/Installing_SciPy/Linux
# SciPy 0.9.0 (http://www.scipy.org/)	http://www.scipy.org/Installing_SciPy/Linux
# Cython 0.15 (http://www.cython.org/)
# wxPython 2.8 <http://www.wxpython.org/>
# Matplotlib 1.0.1 (http://matplotlib.sourceforge.net/)
# IPython 0.11 <http://ipython.org/>
# Nose 1.1.2 <http://readthedocs.org/docs/nose/en/latest/> (Only needed to runthe test suits.)
install_python_packages() {

	

	printStep 'Install JPype'
	#mkdir $HOME/local
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/JPype-0.5.4.2.zip $WORKINGDIR/JPype-0.5.4.2.zip
	unzip JPype-0.5.4.2.zip
	cd JPype-0.5.4.2
	#python2.7 setup.py install --prefix $HOME/local
	python2.7 setup.py install

	export PYTHONPATH=$PYTHONPATH:/opt/python2.7.2/lib/python2.7/site-packages

	#printStep 'Test JPype'
	#python2.7
	#import jpype


	printStep 'Install lxml'	
	yum -y install libxslt-devel
	#python2.7 setup.py install
	

	
	#export BLAS=/path/to/libblas.so
	#export LAPACK=/path/to/liblapack.so
	#export ATLAS=/path/to/libatlas.so

	printStep 'Install NumPy'
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/numpy-1.6.1.zip $WORKINGDIR/numpy-1.6.1.zip
	unzip numpy-1.6.1.zip
	rm -f numpy-1.6.1.zip
	cd numpy-1.6.1
	python2.7 setup.py install
	
	
	printStep 'Install SciPy'
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/scipy-0.10.0.tar.gz $WORKINGDIR/scipy-0.10.0.tar.gz
	tar xvf scipy-0.10.0.tar.gz
	rm -f scipy-0.10.0.tar.gz
	cd scipy-0.10.0
	python2.7 setup.py install

	printStep 'Install Cython'
	cd $WORKINGDIR
	wget http://www.cython.org/release/Cython-0.15.1.tar.gz
	tar xfz Cython-0.15.1.tar.gz
	rm -f Cython-0.15.1.tar.gz
	cd Cython-0.15.1
	python2.7 setup.py install

	printStep 'Install wxPython'
	cd $WORKINGDIR
	wget http://cdnetworks-us-2.dl.sourceforge.net/project/wxpython/wxPython/2.8.12.1/wxPython-src-2.8.12.1.tar.bz2
	tar xvfj wxPython-src-2.8.12.1.tar.bz2
	rm -f wxPython-src-2.8.12.1.tar.bz2
	cd wxPython-src-2.8.12.1/wxPython
	python2.7 setup.py install
	#python2.7 build-wxpython.py --build_dir=../bld

	printStep 'Install ipython-0.12 for python 2.7'
	pip install ipython

	printStep 'Install nose for python 2.7'
	pip install nose
	pip install lxml
	pip install blas

	printStep 'Install FreeType2'
	cd $WORKINGDIR
	wget http://downloads.sourceforge.net/freetype/freetype-2.4.8.tar.bz2
	tar xvf freetype-2.4.8.tar.bz2
	rm -f freetype-2.4.8.tar.bz2
	cd freetype-2.4.8
	./configure --prefix=/usr
	make
	make install

	printStep 'Install libpng-1.2.29'
	cd $WORKINGDIR
	wget http://downloads.sourceforge.net/libpng/libpng-1.2.29.tar.bz2
	tar xvf libpng-1.2.29.tar.bz2
	rm -f libpng-1.2.29.tar.bz2
	cd libpng-1.2.29
	./configure --prefix=/usr
	make
	make install

	printStep 'Install matplotlib'
	cd $WORKINGDIR
	wget http://iweb.dl.sourceforge.net/project/matplotlib/matplotlib/matplotlib-1.1.0/matplotlib-1.1.0.tar.gz
	tar xfz matplotlib-1.1.0.tar.gz
	rm -f matplotlib-1.1.0.tar.gz
	cd matplotlib-1.1.0
	python2.7 setup.py build
	python2.7 setup.py install

}






make_startupLinks() {

	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc0.d/K91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc1.d/K91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc2.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc3.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc4.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc5.d/S91straylight
	ln -s /etc/init.d/straylight.sh /etc/rc.d/rc6.d/K91straylight
}


#https://svn.jmodelica.org/tags/1.6/INSTALL
install_jmodelica() {

	printStep 'Install Jmodelica'
	cd $WORKINGDIR

	ln -sf /opt/python2.7.2/bin/python /usr/bin/python

	printStep 'Get Sundials'
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/sundials-2.4.0.tar.gz $WORKINGDIR/sundials-2.4.0.tar.gz
	tar xfz sundials-2.4.0.tar.gz
	cd sundials-2.4.0
	./configure
	make
	make install
	
	#installs some to /usr/local/jmodelica
	printStep 'Build JModelica'
	cd $WORKINGDIR
	svn checkout --trust-server-cert --non-interactive https://svn.jmodelica.org/tags/1.6/ JModelica
	cd $WORKINGDIR/JModelica
	
	mkdir build
	cd $WORKINGDIR/JModelica/build

	$WORKINGDIR/JModelica/configure --with-ipopt=$WORKINGDIR/CoinIpopt --with-sundials=/usr/local
	make
	make install
	#make docs

	ln -sf /usr/lib/python2.6 /usr/bin/python
}

install_ipopt() {
	
	printStep 'Get Ipopt'
	cd $WORKINGDIR
	wget http://www.coin-or.org/download/source/Ipopt/Ipopt-3.10.1.tgz
	tar xfz Ipopt-3.10.1.tgz
	rm -f Ipopt-3.10.1.tgz
	mv Ipopt-3.10.1 CoinIpopt

	printStep 'Get Blas'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/Blas
	./get.Blas
	
	printStep 'Get Lapack'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/Lapack
	./get.Lapack

	printStep 'Get ASL'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/ASL
	./get.ASL
	
	printStep 'Get MA27'
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/ma27-1.0.0.tar.gz $WORKINGDIR/ma27-1.0.0.tar.gz
	tar xfz ma27-1.0.0.tar.gz
	rm -f ma27-1.0.0.tar.gz
	cp $WORKINGDIR/ma27-1.0.0/src/ma27d.f $WORKINGDIR/CoinIpopt/ThirdParty/HSL/ma27d.f

	printStep 'Get MC19'
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/mc19-1.0.0.tar.gz $WORKINGDIR/mc19-1.0.0.tar.gz
	tar xfz mc19-1.0.0.tar.gz
	rm -f mc19-1.0.0.tgz
	cp $WORKINGDIR/mc19-1.0.0/src/mc19d.f $WORKINGDIR/CoinIpopt/ThirdParty/HSL/mc19d.f

	printStep 'Make HSL'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/HSL/
	./configure -enable-loadable-library
	make install

	printStep 'Install Ipopt - configure'
	cd $WORKINGDIR/CoinIpopt

	./configure
	# output should be: "configure: Main configuration of Ipopt successful"
	printStep 'Install Ipopt - make'
	make
	printStep 'Install Ipopt - make check'
	make check
	printStep 'Install Ipopt - make install'
	make install

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

install_gcc() {




	#installs in /usr/local/lib
	printStep 'install_gcc g77'
	cd $WORKINGDIR
	wget http://fileboar.com/gcc/releases/gcc-4.6.2/gcc-4.6.2.tar.bz2
	tar xvfj gcc-4.6.2.tar.bz2
	rm -f gcc-4.6.2.tar.bz2
	cd gcc-4.6.2
	./configure
	make
	make install

	exit 0


}


test2() {

	install_gcc
	exit 0


	printStep 'install MPFR version 2.4.2'
	cd $WORKINGDIR
	wget http://www.mpfr.org/mpfr-2.4.2/mpfr-2.4.2.tar.bz2
	tar xvfj mpfr-2.4.2.tar.bz2
	rm -f mpfr-2.4.2.tar.bz2
	cd mpfr-2.4.2
	./configure
	make
	make install


	printStep 'install mpc'
	cd $WORKINGDIR
	wget http://www.multiprecision.org/mpc/download/mpc-0.9.tar.gz
	tar xvf mpc-0.9.tar.gz
	rm -f mpc-0.9.tar.gz
	cd mpc-0.9
	./configure --prefix=/usr/local/
	make
	make install

	printStep 'install gmp-4.3.2'
	cd $WORKINGDIR
	wget ftp://ftp.gnu.org/gnu/gmp/gmp-4.3.2.tar.bz2
	tar xvfj gmp-4.3.2.tar.bz2
	rm -f gmp-4.3.2.tar.bz2
	cd gmp-4.3.2
	./configure
	make
	make install



	printStep 'Install BLAS'
	cd $WORKINGDIR
	wget http://www.netlib.org/blas/blas.tgz
	tar xzf blas.tgz
	cd BLAS
	g77 -O2 -fno-second-underscore -c *.f                     # with g77
	gfortran -O2 -std=legacy -fno-second-underscore -c *.f    # with gfortran
	ar r libfblas.a *.o
	ranlib libfblas.a
	rm -rf *.o
	export BLAS=$WORKINGDIR/BLAS/libfblas.a

	# NOTE: The selected fortran compiler must be consistent for BLAS, LAPACK, NumPy, and SciPy.
	# For GNU compiler on 32-bit systems:
	

	exit 0
	printStep 'Install SciPy'
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/scipy-0.10.0.tar.gz $WORKINGDIR/scipy-0.10.0.tar.gz
	tar xvf scipy-0.10.0.tar.gz
	rm -f scipy-0.10.0.tar.gz
	cd scipy-0.10.0
	python2.7 setup.py install

	#http://www.scipy.org/Installing_SciPy/BuildingGeneral
	printStep 'Install blas'
	cd $WORKINGDIR
	wget http://www.netlib.org/blas/blas.tgz
	tar xzvf blas.tgz
	rm -f blas.tgz
	cd BLAS
	python2.7 setup.py install




}


case "$1" in
        'all')
		precheck
		info
		installDependencies
		setEnvironmentVars
		cloneGitRepo
		install_python
		install_gtk
		mavenBuild
		mkDirs
		copy_binaries
		copy_startup_scripts
		make_startupLinks
	;;
        'clone')
		precheck
		cloneGitRepo
        ;;
        'update')
		./straylight.sh stop
		precheck
		clean
		mkDirs
		updateGitRepo
		mavenBuild
		copy_binaries
		copy_startup_scripts
		./straylight.sh start
        ;;
        'env')
		precheck
		setEnvironmentVars
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
		test2
        ;;
        'python')
		install_python
        ;;	
        'jmodelica')
		installDependencies
		cloneGitRepo
		install_ipopt
        ;;
        *)
	usage
esac



exit 0