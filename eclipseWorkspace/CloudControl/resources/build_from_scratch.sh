# build

STEPNUMBER=0
printStep()
{ 
	STEPNUMBER=$[$STEPNUMBER+1]
	echo '*************************************'
	echo '       				 '
	echo '     STEP '$STEPNUMBER' - '$1
	echo '       				 '
	echo '*************************************'
} 


printStep 'info'
echo 'whoami:'
whoami
python --version
java -version
javac -version


#yum should be installed
printStep 'yum --version'

yum --version

printStep 'add jpackage to yum repos'
cd /etc/yum.repos.d/
sudo wget http://jpackage.org/jpackage.repo

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
ln -s apache-maven-2.2.1 maven
/usr/local/maven/bin/mvn -version

printStep 'Set environment variables'
echo 'JAVA_HOME: ' $JAVA_HOME
echo 'M2_HOME: ' $M2_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/jre-1.6.0-openjdk' >> ~/.bashrc
echo 'export M2_HOME=/usr/local/apache-maven-2.2.1' >> ~/.bashrc
echo 'export PATH=${M2_HOME}/bin:${PATH}' >> ~/.bashrc
cat ~/.bashrc
. ~/.bashrc
echo 'JAVA_HOME: ' $JAVA_HOME
echo 'M2_HOME: ' $M2_HOME
echo 'mvn -version:'
mvn -version

printStep 'Clone the git repository'
cd ~
git clone git://github.com/rajdye/straylight.git straylight_repo
#checkout the desired build
cd ~/straylight_repo/eclipseWorkspace/StrayLight

printStep 'Build projects with Maven'
mvn clean install

cd ~/straylight_repo/eclipseWorkspace/StrayLight/SocketServer
mvn clean install

cd ~/straylight_repo/eclipseWorkspace/StrayLight/PageServer
mvn clean install


printStep 'Launch Projects'
cd ~/straylight_repo/eclipseWorkspace/StrayLight/PageServer
mvn exec:java -Dexec.mainClass="com.sri.straylight.pageserver.Main" &

cd ~/straylight_repo/eclipseWorkspace/StrayLight/SocketServer
mvn exec:java -Dexec.mainClass="com.sri.straylight.socketserver.Main" &

exit 0



