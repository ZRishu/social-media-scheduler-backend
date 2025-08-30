## --- Stage 1: Build the application ---
#FROM maven:3.9.6-eclipse-temurin-21 AS build
#
#WORKDIR /app
#
## 1. Copy only the pom.xml to leverage Docker cache
#COPY pom.xml .
#
## 2. Download all dependencies into a cached layer
#RUN mvn dependency:go-offline
#
## 3. Copy your source code
#COPY src ./src
#
## 4. Build the project (tests are often skipped in image builds)
#RUN mvn clean package -DskipTests
#
#
## --- Stage 2: Create the final, lightweight image ---
#FROM eclipse-temurin:21-jre-jammy
#
#WORKDIR /app
#
#COPY --from=build /app/target/*.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]