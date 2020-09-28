FROM openjdk:8-jre-slim
MAINTAINER husen <1178515826@qq.com>
# 设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENV PARAMS="-Xms256 -Xmx256m"

ADD target/blog-server*.jar /app.jar

ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=prod -jar $JAVA_OPTS /app.jar $PARAMS"]

EXPOSE 10003
