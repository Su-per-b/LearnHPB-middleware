REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 

cmd /c mvn package

copy straylight.keystore target\straylight.keystore
cd target
del SocketServer-*shaded.jar


jarsigner -keystore straylight.keystore SocketServer-*.jar straylightSelfSignedAlias -storepass time57
jarsigner -verify SocketServer-*.jar
pause

copy SocketServer-*.jar ..\published\

cd ..\
copy run.template.txt published\run.bat

cd published

"%JAVA_HOME%\bin\java" -Xss2048 -jar SocketServer-0.8.0.jar
pause