# Этап сборки приложения
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /opt/app

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn test

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

# Установка необходимых утилит и PostgreSQL client 16
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        gnupg \
        ca-certificates && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | gpg --dearmor -o /usr/share/keyrings/postgresql-archive-keyring.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/postgresql-archive-keyring.gpg] http://apt.postgresql.org/pub/repos/apt/ jammy-pgdg main" > /etc/apt/sources.list.d/pgdg.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        postgresql-client-16 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /opt/app

COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

# Создаем директорию для бэкапов
RUN mkdir -p /opt/backups/
VOLUME /opt/backups/

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
