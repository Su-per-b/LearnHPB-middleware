REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
 REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 
 
cmd /c mvn package
pause
copy testKeys target\testKeys
cd target
del SocketServer-*shaded.jar
jarsigner -keystore testKeys Client-*.jar jdc -storepass jollyroger
copy SocketServer-*.jar ..\
cd ..\
copy ..\..\visualStudioWorkspace\bin\Debug\expat.dll expat.dll
copy ..\..\visualStudioWorkspace\bin\Debug\FMUwrapper.dll FMUwrapper.dll

"%JAVA_HOME%\bin\java" -Xss2048 -jar SocketServer-0.5.3.jar
pause