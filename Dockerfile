
FROM maven:3.8.5-openjdk-17 AS build

ARG MYSQLHOST
ENV MYSQLHOST=${MYSQLHOST}
ARG MYSQLPORT
ENV MYSQLPORT=${MYSQLPORT}
ARG MYSQLDATABASE
ENV MYSQLDATABASE=${MYSQLDATABASE}
ARG MYSQLUSER
ENV MYSQLUSER=${MYSQLUSER}
ARG MYSQL_ROOT_PASSWORD
ENV MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
ARG USERNAME
ENV USERNAME=${USERNAME}
ARG PASSWORD
ENV PASSWORD=${PASSWORD}

COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Trivia-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]



