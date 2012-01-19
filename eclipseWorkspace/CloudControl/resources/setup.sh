#!/bin/bash
# run with: sudo ./setup.sh test
set -e #exit script if an error occurs


############################ OPTIONS ######################################################
WORKINGDIR='/var/tmp'
GIT_REPOSITORY_REMOTE='git://github.com/rajdye/straylight.git'        	# Url for the git repository
GIT_REPOSITORY_LOCAL="$WORKINGDIR/straylight_repo"

SERVER_NAME='Straylight - Ubuntu'

USER_NAME='ubuntu'                                            # User running server
GROUP_NAME="$USER_NAME"
USER_HOME="/home/$USER_NAME"
LOGFILE='/var/log/straylight.log'                              	# Logfile location and file
ROOT_HOME='/root'
PAUSE=false 							#pause before executing each step for debugging


STRAYLIGHT_INSTALL_DIR='/opt/packages/sri/straylight'
MAVEN_DIR='/usr/bin'

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
	javac -version
}

precheck() {
	#printStep 'precheck'
	USER=$(whoami)
	echo ""
	if [ "$USER" == "root" ]
	then
		echo "You are logged in as user: $USER"	
	else
		echo "You are incorrectly running this script as user:$USER"
		usage
	  #echo "$(date +"%b %a %d  %H:%M:%S"): You do not have Sudo installed." >> $LOGFILE
		exit 1
	fi

	echo ""
}

inst() {

	printStep "Install $1"
	apt-get -y install $1

}

installDependencies() {

	printStep 'Update apt-get'
	apt-get -y update

	inst build-essential
	inst git
	inst openjdk-6-jdk
	inst maven2
	inst subversion
	inst libatlas-base-dev
	inst libgtk2.0-dev
	inst python-gtk2
	inst python-gtk2-dev
	inst cmake
	inst gfortran
	inst libgtk2.0-dev 
	inst libgtk-3-dev
	inst swig
	inst ant
	inst liblapack-dev
	inst libsundials-*
	inst coinor-libipopt-dev
	inst mpich-bin
	inst libblas-dev
	inst libmumps-seq-4.9.2
	#inst apt-get -y install swig1.3
	inst octave3.2-headers

	updatedb

}


cloneGitRepo() {
	printStep 'Clone the git repository'
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
	$MAVEN_DIR/mvn clean install

	cd $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/SocketServer
	$MAVEN_DIR/mvn clean install

	cd $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight/PageServer
	$MAVEN_DIR/mvn clean install

}




copy_binaries() {
	printStep 'Copy Binaries'
	
	if [ ! -d /opt/packages/sri ]
	then
		mkdir /opt/packages/sri
	fi


	echo "cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight $STRAYLIGHT_INSTALL_DIR"
	cp -R $GIT_REPOSITORY_LOCAL/eclipseWorkspace/StrayLight  $STRAYLIGHT_INSTALL_DIR

	rm -Rf $STRAYLIGHT_INSTALL_DIR/PageServer/src
	rm -Rf $STRAYLIGHT_INSTALL_DIR/SocketServer/src
	rm -Rf $STRAYLIGHT_INSTALL_DIR/Common
	

	#config matplotlib
	#rm -f /usr/local/lib/python2.7/dist-packages/matplotlib/mpl-data/matplotlibrc
	cp -f $GIT_REPOSITORY_LOCAL/assets/config/matplotlib/matplotlibrc /usr/local/lib/python2.7/dist-packages/matplotlib/mpl-data/matplotlibrc

	cp -f $GIT_REPOSITORY_LOCAL/assets/test_bouncingBall.py /opt/packages/jmodelica/jmodelica-install/test_bouncingBall.py
	chmod 775 /opt/packages/jmodelica/jmodelica-install/test_bouncingBall.py

	cp -f $GIT_REPOSITORY_LOCAL/assets/bouncingBall.fmu /opt/packages/jmodelica/jmodelica-install/bouncingBall.fmu
}





#clean before upgrade
clean() {
	printStep 'clean'
	
	rm -Rf $STRAYLIGHT_INSTALL_DIR
	
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

		export JAVA_HOME=/usr/lib/jvm/java-6-openjdk
		export M2_HOME=/usr/bin

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

	printStep 'Install Python Setup Tools'
	cd $WORKINGDIR
	wget http://pypi.python.org/packages/2.7/s/setuptools/setuptools-0.6c11-py2.7.egg
	chmod 775 setuptools-0.6c11-py2.7.egg
	sh setuptools-0.6c11-py2.7.egg
	
	/usr/local/bin/easy_install pip
	pip install virtualenv

	printStep 'Install python-dev'
	apt-get -y install python-dev

	printStep 'Install wxPython 2.8'
	apt-get -y install python-wxgtk2.8
	apt-get -y install python-wxtools
	apt-get -y install wx2.8-i18n
	apt-get -y install libwxgtk2.8-dev
	
	printStep 'Install JPype'
	apt-get -y install python-jpype

	printStep 'Install libxslt-dev'
	apt-get -y install libxslt-dev

	pip install numpy
	pip install scipy
	pip install cython

	printStep 'Install ipython-0.12'
	pip install ipython

	printStep 'Install libpng-1.2.29'
	cd $WORKINGDIR
	wget http://downloads.sourceforge.net/libpng/libpng-1.2.29.tar.bz2
	tar xvf libpng-1.2.29.tar.bz2
	rm -f libpng-1.2.29.tar.bz2
	cd libpng-1.2.29
	./configure --prefix=/usr
	make
	make install

	printStep 'Install nose'
	pip install nose

	printStep 'Install lxml'
	pip install lxml

	printStep 'Install matplotlib'
	pip install matplotlib


	
	#pyreadline

}

# http://sourceforge.net/apps/trac/casadi/wiki/InstallationInstructions
install_casadi() {
	
	printStep 'Install CasADi'
	cd /opt/packages

 	svn co https://casadi.svn.sourceforge.net/svnroot/casadi/trunk casadi
	#svn co https://casadi.svn.sourceforge.net/svnroot/casadi/tags/v1.3.0beta/ casadi

	cd casadi
	mkdir build
	cd build
	cmake ../ -DEXTRA_LIBRARIES:STRING=-lgfortran
	make python
}



test_jmodelica() {

	if [ -d ~/scripts ]; then
		echo "Delete scripts directory"
		rm -Rf ~/scripts
	fi

	cp -R $GIT_REPOSITORY_LOCAL/assets/scripts ~/scripts
	cp /var/tmp/JModelica/build/Python/jm_python.sh ~/scripts/jm_python.sh

	chown $USER_NAME:$USER_GROUP -R ~/scripts
	chmod 775 -R ~/scripts
	cd ~/scripts
	
	sh ./check_packages.sh
	sh ./test_fmu.sh
}



#https://svn.jmodelica.org/tags/1.6/INSTALL
install_jmodelica() {

	printStep 'checkout JModelica'
	cd $WORKINGDIR
	svn checkout --trust-server-cert --non-interactive https://svn.jmodelica.org/tags/1.6/ JModelica
	cd $WORKINGDIR/JModelica
	
	mkdir build
	cd $WORKINGDIR/JModelica/build

	printStep 'configure JModelica'
	$WORKINGDIR/JModelica/configure \
	--prefix=/opt/packages/jmodelica/jmodelica-install \
	--with-ipopt=/opt/packages/coin-or/ipopt-install \
	--with-sundials=/opt/packages/sundials/sundials-install \
	--with-casadi=/opt/packages/casadi/build/swig/python
	
	printStep 'make JModelica'
	make
	
	printStep 'printStep 'make JModelica' JModelica'
	make install
	#make docs

}

install_sundials() {
	printStep 'Install Sundials'
	cd $WORKINGDIR
	cp $GIT_REPOSITORY_LOCAL/assets/libs/sundials-2.4.0.tar.gz $WORKINGDIR/sundials-2.4.0.tar.gz
	tar xfz sundials-2.4.0.tar.gz
	cd sundials-2.4.0
	./configure prefix=/opt/packages/sundials/sundials-install
	make
	make install

	#should end with Libraries have been installed in: 
	# /opt/packages/sundials/sundials-install/lib
}


install_ipopt() {
	
	printStep 'Get Ipopt'
	cd $WORKINGDIR
	wget http://www.coin-or.org/download/source/Ipopt/Ipopt-3.10.1.tgz
	#wget http://www.coin-or.org/download/source/Ipopt/Ipopt-doxydoc-3.9.3.tgz
	tar xfz Ipopt-3.10.1.tgz
	rm -f Ipopt-3.10.1.tgz
	mv Ipopt-3.10.1 CoinIpopt

	printStep 'Get Blas'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/Blas
	./get.Blas
	
	printStep 'Get Lapack'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/Lapack
	./get.Lapack

	printStep 'Get Mumps'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/Mumps
	./get.Mumps

	printStep 'Get ASL'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/ASL
	./get.ASL
	
	printStep 'Get Metis'
	cd $WORKINGDIR/CoinIpopt/ThirdParty/Metis
	./get.Metis

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

	./configure --disable-pkg-config \
	prefix=/opt/packages/coin-or/ipopt-install # was defaulting to --prefix=/var/tmp/CoinIpopt
	# output should be: "configure: Main configuration of Ipopt successful"
	printStep 'Install Ipopt - make'
	make

	printStep 'Install Ipopt - make check'
	make check

	printStep 'Install Ipopt - make install'
	make install
	#/var/tmp/CoinIpopt/lib

}


usage() {
	echo "Usage: sudo ./setup all|clone|env|build|precheck|clean|test|update"	
}

case "$1" in
        'all')
		precheck
		installDependencies
		setEnvironmentVars
		cloneGitRepo
		install_python_packages
		install_sundials
		install_ipopt
		install_casadi
		install_jmodelica
		test_jmodelica
		mavenBuild
		copy_binaries
	;;
        'clone')
		precheck
		cloneGitRepo
        ;;
        'update')
		precheck
		clean
		updateGitRepo
		mavenBuild
		copy_binaries
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
		updateGitRepo
		clean
		copy_binaries

        ;;
        *)
		usage
	;;

	
esac



exit 0