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
PAUSE=true
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

	printStep 'install maven2'
	cd ~
	wget http://apache.cyberuse.com//maven/binaries/apache-maven-2.2.1-bin.tar.gz
	mv apache-maven-2.2.1-bin.tar.gz /usr/local
	cd /usr/local
	tar -zxvf apache-maven-2.2.1-bin.tar.gz
	rm -f apache-maven-2.2.1-bin.tar.gz
	ln -s apache-maven-2.2.1 maven
	/usr/local/maven/bin/mvn -version

	printStep 'Development Tools'

	yum -y groupinstall 'Development Tools'
	yum -y install openssl-devel* zlib*.x86_64
}




cloneGitRepo() {
	printStep 'Clone the git repository'
	cd ~
	git clone $GIT_REPOSITORY straylight_repo
}


updateGitRepo() {
	printStep 'Update the git repository'
	cd ~/straylight_repo
	git pull
}


# go to the srouce code folder and build binaries with Maven
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

mkDirs() {
	printStep 'Make Directories'
	mkdir /usr/local/straylight 
	mkdir /usr/local/straylight/pageserver /usr/local/straylight/pageserver/target
	mkdir /usr/local/straylight/socketserver /usr/local/straylight/socketserver/target
}


copy_binaries() {
	printStep 'Copy Binaries'

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
	echo "Usage: sudo ./setup all|clone|env|build|precheck|clean|test|update"	
}

#clean before upgrade
clean() {
	printStep 'clean'
	
	#remove all binaries
	rm -Rf /usr/local/straylight
	
	#remove scripts
	rm -f /etc/init.d/straylight.sh
	rm -f $USER_HOME/straylight.sh
	rm -f $USER_HOME/setup.sh


}

#after clean, then remove everything
uninstall() {
	rm -Rf ~/straylight_repo


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
	printStep 'install_python'
	



	printStep 'Install SQL Lite'
	cd /var/tmp
	wget http://sqlite.org/sqlite-amalgamation-3.7.3.tar.gz
	tar xfz sqlite-amalgamation-3.7.3.tar.gz
	rm -f ./sqlite-amalgamation-3.7.3.tar.gz
	cd sqlite-3.7.3/
	./configure
	make
	make install
	
	
	printStep 'Install Python-2.7.2'
	cd /var/tmp
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
	
	ldconfig

	printStep 'Install Python Setup Tools'
	cd /var/tmp
	wget http://pypi.python.org/packages/2.7/s/setuptools/setuptools-0.6c11-py2.7.egg
	chmod 775 setuptools-0.6c11-py2.7.egg
	sh setuptools-0.6c11-py2.7.egg --prefix=/opt/python2.7.2
	#This should install the egg here: /opt/python2.7.2/site-packages/

	/opt/python2.7.2/bin/easy_install pip
	ln -sf /opt/python2.7.2/bin/pip /usr/bin/pip

	pip install virtualenv
	ln -sf /opt/python2.7.2/bin/virtualenv /usr/bin/virtualenv


}


# JPype 0.5.4.2 (http://jpype.sourceforge.net/) http://hep.phys.utk.edu/BRM_Interface/index.php/Installing_JPype
# lxml 2.3 (http://codespeak.net/lxml/) 
# NumPy 1.6.1 (http://numpy.scipy.org/) http://www.scipy.org/Installing_SciPy/Linux
# SciPy 0.9.0 (http://www.scipy.org/)	http://www.scipy.org/Installing_SciPy/Linux
# Cython 0.15 (http://www.cython.org/)
# Matplotlib 1.0.1 (http://matplotlib.sourceforge.net/)
# wxPython 2.8 <http://www.wxpython.org/>
# IPython 0.11 <http://ipython.org/>
# Nose 1.1.2 <http://readthedocs.org/docs/nose/en/latest/> (Only needed to runthe test suits.)
install_python_packages() {

	

	printStep 'Install JPype'
	mkdir $HOME/local
	cd /var/tmp
	cp /root/straylight_repo/assets/libs/JPype-0.5.4.2.zip /var/tmp/JPype-0.5.4.2.zip
	unzip JPype-0.5.4.2.zip
	cd JPype-0.5.4.2
	python setup.py install --prefix $HOME/local

	export PYTHONPATH=$PYTHONPATH:$HOME/local/lib/python2.5/site-packages



	#yum -y python-dev



	printStep 'Install lxml'	
	yum -y install libxslt-devel
	python setup.py install
	


	#export BLAS=/path/to/libblas.so
	#export LAPACK=/path/to/liblapack.so
	#export ATLAS=/path/to/libatlas.so

	printStep 'Install NumPy'
	cd /var/tmp
	cp /root/straylight_repo/assets/libs/numpy-1.6.1.zip /var/tmp/numpy-1.6.1.zip
	unzip numpy-1.6.1.zip
	cd numpy-1.6.1
	python setup.py install --user
	
	
	printStep 'Install SciPy'
	cp /root/straylight_repo/assets/libs/scipy-0.10.0.tar.gz /var/tmp/scipy-0.10.0.tar.gz
	unzip scipy-0.10.0.zip
	cd scipy-0.10.0
	python setup.py install --user


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
	cd /var/tmp
	
	#yum -y install blas
	#yum -y install lapack
	
	#https://projects.coin-or.org/Ipopt/browser/releases/3.10.1/INSTALL
	printStep 'Get Ipopt'
	wget http://www.coin-or.org/download/source/Ipopt/Ipopt-3.10.1.tgz
	tar xfz Ipopt-3.10.1.tgz
	rm -f Ipopt-3.10.1.tgz
	mv Ipopt-3.10.1 CoinIpopt



	printStep 'Get Blas'
	cd /var/tmp/CoinIpopt/ThirdParty/Blas
	./get.Blas
	
	printStep 'Get Lapack'
	cd /var/tmp/CoinIpopt/ThirdParty/Lapack
	./get.Lapack

	printStep 'Get ASL'
	cd /var/tmp/CoinIpopt/ThirdParty/ASL
	./get.ASL
	
	printStep 'Make MA27'
	cd /var/tmp
	cp /root/straylight_repo/assets/libs/ma27-1.0.0.tar.gz /var/tmp/ma27-1.0.0.tar.gz
	tar xfz ma27-1.0.0.tar.gz
	rm -f ma27-1.0.0.tar.gz
	cd /var/tmp/ma27-1.0.0
	./configure
	make
	make install
	cp /var/tmp/ma27-1.0.0/src/ma27d.f /var/tmp/CoinIpopt/ThirdParty/HSL/ma27d.f

	
	printStep 'Make MC19.'
	cd /var/tmp
	cp /root/straylight_repo/assets/libs/mc19-1.0.0.tar.gz /var/tmp/mc19-1.0.0.tar.gz
	tar xfz mc19-1.0.0.tar.gz
	rm -f mc19-1.0.0.tgz
	#cd mc19-1.0.0
	#./configure
	#make
	cp /var/tmp/mc19-1.0.0/src/mc19d.f /var/tmp/CoinIpopt/ThirdParty/HSL/mc19d.f


	printStep 'Install Ipopt'
	mkdir /var/tmp/CoinIpopt/build
	cd /var/tmp/CoinIpopt/build

	/var/tmp/CoinIpopt/configure
	# output should be: "configure: Main configuration of Ipopt successful"
	make
	make test
	make install
	
	printStep 'Get Sundials'
	cd /var/tmp
	cp /root/straylight_repo/assets/libs/sundials-2.4.0.tar.gz /var/tmp/sundials-2.4.0.tar.gz
	tar xfz sundials-2.4.0.tar.gz
	cd sundials-2.4.0
	./configure
	make
	make install

	printStep 'Build JModelica'
	cd /var/tmp
	svn checkout --trust-server-cert --non-interactive https://svn.jmodelica.org/tags/1.6/ JModelica
	cd /var/tmp/JModelica
	mkdir build
	cd /var/tmp/JModelica/build
	/var/tmp/JModelica/configure --with-ipopt=/var/tmp/CoinIpopt/Ipopt --with-sundials=/var/tmp/sundials-2.4.0
	make
	make install
	#make docs 

}

install_ipopt() {
	
	printStep 'Get Ipopt'
	cd /var/tmp
	wget http://www.coin-or.org/download/source/Ipopt/Ipopt-3.10.1.tgz
	tar xfz Ipopt-3.10.1.tgz
	rm -f Ipopt-3.10.1.tgz
	mv Ipopt-3.10.1 CoinIpopt


	printStep 'Get Blas'
	cd /var/tmp/CoinIpopt/ThirdParty/Blas
	./get.Blas
	
	printStep 'Get Lapack'
	cd /var/tmp/CoinIpopt/ThirdParty/Lapack
	./get.Lapack

	printStep 'Get ASL'
	cd /var/tmp/CoinIpopt/ThirdParty/ASL
	./get.ASL
	
	printStep 'Get MA27'
	cd /var/tmp
	cp /root/straylight_repo/assets/libs/ma27-1.0.0.tar.gz /var/tmp/ma27-1.0.0.tar.gz
	tar xfz ma27-1.0.0.tar.gz
	rm -f ma27-1.0.0.tar.gz
	cp /var/tmp/ma27-1.0.0/src/ma27d.f /var/tmp/CoinIpopt/ThirdParty/HSL/ma27d.f

	printStep 'Get MC19'
	cd /var/tmp
	cp /root/straylight_repo/assets/libs/mc19-1.0.0.tar.gz /var/tmp/mc19-1.0.0.tar.gz
	tar xfz mc19-1.0.0.tar.gz
	rm -f mc19-1.0.0.tgz
	cp /var/tmp/mc19-1.0.0/src/mc19d.f /var/tmp/CoinIpopt/ThirdParty/HSL/mc19d.f

	cd /var/tmp/CoinIpopt/ThirdParty/HSL/
	./configure -enable-loadable-library
	make install

	printStep 'Install Ipopt - configure'
	#mkdir /var/tmp/CoinIpopt/build
	cd /var/tmp/CoinIpopt

	./configure
	# output should be: "configure: Main configuration of Ipopt successful"
	printStep 'Install Ipopt - make'
	make
	printStep 'Install Ipopt - make check'
	make check
	printStep 'Install Ipopt - make install'
	make install

}






case "$1" in
        'all')
		precheck
		info
		installDependencies
		setEnvironmentVars
		cloneGitRepo
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
		install_ipopt
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