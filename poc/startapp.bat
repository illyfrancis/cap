set m2_repo=c:\Documents and Settings\cy4y423\.m2\repository

java -classpath target\poc-0.0.1.jar;"%m2_repo%\org\apache\camel\camel-spring\2.11.1\camel-spring-2.11.1.jar";"%m2_repo%\org\apache\camel\camel-core\2.11.1\camel-core-2.11.1.jar" com.acme.cap.UtrApplication