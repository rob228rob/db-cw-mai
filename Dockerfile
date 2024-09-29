FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /opt/app

COPY mvnw pom.xml ./

COPY ./src ./src

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /opt/app

COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]