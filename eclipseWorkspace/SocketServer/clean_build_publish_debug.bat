
call clean.bat

cd ..\StraylightParent
cmd /c mvn clean install

cd ..\SocketServer

call copy_native_code.bat
call publish_debug.bat