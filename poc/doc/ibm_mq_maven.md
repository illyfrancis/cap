## Maven insall jar into local repository

#### Example

    mvn install:install-file
      -Dfile="C:\Program Files\IBM\WebSphere MQ\java\lib\<file_name.jar>"
      -DgroupId=com.ibm
      -DartifactId=com.ibm.mqjms
      -Dversion=7.5.0.2
      -Dpackaging=jar
      -DgeneratePom=true

- path-to-file: C:\Program Files\IBM\WebSphere MQ\java\lib\<file-name.jar>
- group-id: com.ibm
- artifact-id: 
    - com.ibm.mqjms
    - com.ibm.mq.jmqi
    - com.ibm.disthub2.dhbcore (?) different between the two examples
- version: 7.5.0.2
- packaging: jar


#### Actual scripts

com.ibm.mqjms.jar

    mvn install:install-file
     -Dfile="C:\Program Files\IBM\WebSphere MQ\java\lib\com.ibm.mqjms.jar"
     -DgroupId=com.ibm
     -DartifactId=com.ibm.mqjms
     -Dversion=7.5.0.2
     -Dpackaging=jar
     -DgeneratePom=true

com.ibm.mq.jmqi.jar

    mvn install:install-file
     -Dfile="C:\Program Files\IBM\WebSphere MQ\java\lib\com.ibm.mq.jmqi.jar"
     -DgroupId=com.ibm
     -DartifactId=com.ibm.mq.jmqi
     -Dversion=7.5.0.2
     -Dpackaging=jar
     -DgeneratePom=true

dhbcore.jar

    mvn install:install-file
     -Dfile="C:\Program Files\IBM\WebSphere MQ\java\lib\dhbcore.jar"
     -DgroupId=com.ibm
     -DartifactId=dhbcore
     -Dversion=7.5.0.2
     -Dpackaging=jar
     -DgeneratePom=true

## Messaging with WMQ using Spring JMS

- http://techvj.blogspot.com/2011/03/messaging-with-websphere-mq-using.html

To start off we need to obtain the WebSphere MQ JARs. These JARs are proprietary - hence they will not resolve through a public Maven repository like Maven Central. These JARs need to be obtained from the WebSphere MQ installation directory and manually deployed to our local Maven repository. The config below defines the WebSphere MQ dependencies in our Maven POM file.

    <dependency>
        <groupId>com.ibm</groupId>
        <artifactId>com.ibm.mqjms</artifactId>
        <version>${webSphereMQVersion}</version>
    </dependency>
    <dependency>
        <groupId>com.ibm</groupId>
        <artifactId>com.ibm.mq.jmqi</artifactId>
        <version>${webSphereMQVersion}</version>
    </dependency>
    <dependency>
        <groupId>com.ibm</groupId>
        <artifactId>com.ibm.disthub2.dhbcore</artifactId>
        <version>${webSphereMQVersion}</version>
    </dependency>

The last dependency is different to example below, and I couldn't find `com.ibm.disthub2.dhbcore.jar` from IBM MQ installation directory.

## Jar for IBM MQ

Refer to this [blog question](http://camel.465427.n5.nabble.com/JAR-for-IBM-MQ-td5596719.html). In it, it says :-

To use IBM MQ, I have added following in my POM:

    <dependency>  
      <groupId>com.ibm.mq</groupId>  
      <artifactId>com.ibm.mq.jmqi</artifactId>
      <version>7.0.1.3</version>
      <scope>compile</scope>  
    </dependency>  
        
    <dependency>  
      <groupId>com.ibm</groupId>  
      <artifactId>com.ibm.mqjms</artifactId>
      <version>7.0.1.3</version>  
      <scope>compile</scope>  
    </dependency>  
    
    <dependency>  
      <groupId>com.ibm.mq.dhbcore</groupId>  
      <artifactId>dhbcore</artifactId>
      <version>7.0.1.3</version>  
      <scope>compile</scope>  
    </dependency>  

## Other stuff, could be useful

#### Camel doc, setting JMS provider options for WMQ

http://camel.apache.org/jms.html#JMS-SettingJMSprovideroptionsonthedestination

#### Not related to Camel directly but an example for using Spring with MQ

http://www.ibm.com/developerworks/web/library/wa-spring4/

#### Another related article

http://lowry-techie.blogspot.com/2010/11/camel-integration-with-websphere-mq.html

#### Spring configs for MQ

http://www.simplethoughtsonline.com/2012/01/spring-and-ibm-mq-series.html

# Connection pooling

#### Some notes

Mostly it talks about using the Spring's CachingConnectionFactory

- http://stackoverflow.com/questions/12243574/spring-jmswebsphere-mq
- http://forum.spring.io/forum/spring-projects/integration/jms/89532-defaultmessagelistenercontainer-cachingconnectionfactory-tomcat-and-websphere-mq
- http://grokbase.com/t/camel/users/124dfe115v/new-jms-connection-being-created-for-every-message