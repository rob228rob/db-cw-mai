
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

COPY ./mvnw .
COPY ./.mvn .mvn
COPY ./pom.xml .
COPY ./src ./src

RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests=true

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV JAVA_OPTS=""

EXPOSE 8084

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
