# STAGE 1: Build the application
# Use an official Maven image with JDK 21 (as specified in your pom.xml)
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copy POM and download dependencies first
# This step is cached by Docker, so it only re-runs if pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copy the rest of the source code
COPY src ./src

# 3. Build the application and skip tests
RUN mvn clean package -DskipTests

# STAGE 2: Create the final, lightweight production image
# Use a slim JRE for a smaller footprint and better security
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 4. Copy the built .jar file from the 'builder' stage
# This wildcard finds the compiled -SNAPSHOT.jar file
COPY --from=builder /app/target/*-SNAPSHOT.jar app.jar

# 5. Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]