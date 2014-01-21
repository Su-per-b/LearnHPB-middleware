REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
 REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 
 
cmd /c mvn package
pause
copy testKeys target\testKeys
cd target
del Client-*shaded.jar
jarsigner -keystore testKeys Client-*.jar jdc -storepass jollyroger
copy Client-*.jar ..\
cd ..\
copy ..\..\visualStudioWorkspace\bin\Debug\expat.dll native_code\expat.dll
copy ..\..\visualStudioWorkspace\bin\Debug\FMUwrapper.dll native_code\FMUwrapper.dll

java -Xss2048 -jar Client-0.5.3.jar
pause