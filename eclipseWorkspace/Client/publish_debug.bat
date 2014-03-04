REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings


cmd /c mvn package

copy testKeys target\testKeys
cd target
del Client-*shaded.jar
jarsigner -keystore testKeys Client-*.jar jdc -storepass jollyroger
copy Client-*.jar ..\published\

cd ..\
copy run.template.txt published\run.bat

cd published
"%JAVA_HOME%\bin\java" -Xss2048 -jar Client-0.6.0.jar
pause