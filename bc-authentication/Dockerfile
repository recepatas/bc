FROM openjdk:8-jdk-alpine
EXPOSE 8080

ADD target/bc-authentication-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]