FROM openjdk:17-jdk-slim

RUN mkdir /app
COPY app.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]