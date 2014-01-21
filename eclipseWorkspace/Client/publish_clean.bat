REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 
del native_code\expat.dll
del native_code\FMUwrapper.dll

rmdir target /s /q
del *.log
del Client-*.jar


pause