FROM adoptopenjdk/maven-openjdk8 as build-stage

RUN mkdir /mq-bridge
COPY ./ /mq-bridge

WORKDIR /mq-bridge/

RUN mvn clean install -Dmaven.test.skip=true



FROM tomcat:9-jdk8-adoptopenjdk-openj9 as tomcat
COPY setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY /conf/application.properties /usr/local/tomcat/conf/application.properties
COPY --from=build-stage /root/.m2/repository/com/example/WMQExtractor/0.0.2-SNAPSHOT/WMQExtractor-0.0.2-SNAPSHOT.war /usr/local/tomcat/webapps/WMQExtractor-0.0.2-SNAPSHOT.war

ENTRYPOINT catalina.sh run