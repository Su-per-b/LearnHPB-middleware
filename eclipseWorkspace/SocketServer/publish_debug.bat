REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 

cmd /c mvn package

copy testKeys target\testKeys
cd target
del SocketServer-*shaded.jar
jarsigner -keystore testKeys SocketServer-*.jar jdc -storepass jollyroger
copy SocketServer-*.jar ..\published\

cd ..\
copy run.template.txt published\run.bat

cd published

"%JAVA_HOME%\bin\java" -Xss2048 -jar SocketServer-0.5.3.jar
pause