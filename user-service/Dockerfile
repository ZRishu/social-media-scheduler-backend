# --- Stage 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only the pom.xml to leverage Docker's layer cache
COPY pom.xml .

# Download all dependencies
RUN mvn dependency:go-offline

# Copy your source code
COPY src ./src

# Build the project and create the executable JAR, skipping tests
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final, lightweight image ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy only the built JAR from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]