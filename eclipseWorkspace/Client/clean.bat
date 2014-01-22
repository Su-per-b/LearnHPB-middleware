REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings
 
del native_code\expat.dll
del native_code\FMUwrapper.dll
del dependency-reduced-pom.xml

rmdir target /s /q
rmdir published /s /q
rmdir native_code /s /q

del *.log
del Client-*.jar


pause