REM change eclipseWorkspace\Client\src\main\resources\fmuwrapper-config.xml to production settings
REM change eclipseWorkspace\FMUwrapper\src\main\resources\fmuwrapper-config.xml to production settings

call clean.bat

cd ..\StraylightParent
cmd /c mvn clean install

cd ..\Client
pause

call copy_native_code.bat
call publish_debug.bat