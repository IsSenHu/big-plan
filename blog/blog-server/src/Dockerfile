FROM openjdk:8-jre-slim
MAINTAINER husen <1178515826@qq.com>

ENV PARAMS=""

ADD target/blog-server*.jar /app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar $PARAMS"]

EXPOSE 10003
