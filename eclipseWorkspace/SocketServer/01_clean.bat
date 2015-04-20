REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 
rmdir ..\FMUwrapper\native_code /s /q

del dependency-reduced-pom.xml

rmdir target /s /q
rmdir published /s /q
rmdir native_code /s /q
rmdir ..\FMUwrapper\target /s /q


del *.log
del SocketServer-*.jar


pause