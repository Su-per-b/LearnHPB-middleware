rem @echo off
set JAVA="c:\program files\java\jdk1.5.0_02\bin\javaw"
set JAVA_OPTS="-Xmx256M"
set PHOTOVAULT_MAIN_CLASS="photovault.swingui.Photovault"

set LOG4J=lib\log4j-1.2.12.jar
set OJB=lib\db-ojb-1.0.rc5.jar
set CONFDIR=conf
set JAI_LIBS=lib\jai_core.jar;lib\jai_codec.jar

set OJBDEPS=lib\antlr.jar;lib\btree.jar;lib\commons-beanutils.jar;lib\commons-collections.jar;lib\commons-dbcp-1.1.jar;lib\commons-lang-2.0.jar;lib\commons-logging.jar;lib\commons-pool-1.1.jar;lib\db-ojb-1.0.1.jar;lib\hsqldb.jar;lib\j2ee.jar;lib\jakarta-regexp-1.3.jar;lib\jcs.jar;lib\jdo.jar;lib\jdori-enhancer.jar;lib\jdori.jar;lib\p6spy.jar;lib\prevayler.jar;lib\xdoclet-1.2b3-dev.jar;lib\xdoclet-ojb-module-1.2b3-dev.jar;lib\xercesImpl.jar;lib\xjavadoc-1.0.jar;lib\xml-apis.jar

set CLASSPATH=%CONFDIR%;lib\mysql-connector-java.jar;lib\exif_extract.jar;build\;%LOG4J%;%OJB%;%OJBDEPS%;%JAI_LIBS%

%JAVA% -classpath %CLASSPATH% %JAVA_OPTS% %PHOTOVAULT_MAIN_CLASS%
