FROM openjdk:8-jre-slim
MAINTAINER husen <1178515826@qq.com>

ENV PARAMS="-Xms256 -Xmx256m"

ADD target/blog-server*.jar /app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar $PARAMS","--spring.profiles.active=prod","-c"]

EXPOSE 10003
