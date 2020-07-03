FROM java:8
COPY *.jar /app.jar
CMD ["--server.port=9001"]
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "/app.jar"]