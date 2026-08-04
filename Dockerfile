# A tiny Dockerfile for the tutorial calculator app.
# Two stages: build the jar with Maven, then run it on a slim JRE.

# ---- stage 1: build ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---- stage 2: run ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/cicd-tutorial-1.0.0-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
