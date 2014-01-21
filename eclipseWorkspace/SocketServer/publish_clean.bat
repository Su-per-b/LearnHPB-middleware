REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 
del expat.dll
del FMUwrapper.dll

rmdir target /s /q
del *.log
del SocketServer-*.jar


pause