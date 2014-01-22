cd StraylightParent
cmd /c mvn clean install
pause
cd ../Client
mvn exec:java -e -X -Dexec.args="-Xss2048"
pausev