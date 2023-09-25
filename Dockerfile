FROM openjdk:17-jdk-slim

RUN mkdir /app
COPY ./build/libs/soul-core-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]