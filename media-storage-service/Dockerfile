# ======================
# STAGE 1: The "Builder"
# This stage builds the code. It is large but temporary.
# ======================
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copy POM and download dependencies first
# This step is cached by Docker. It only re-runs if pom.xml changes.
COPY pom.xml .
# This uses the "network: host" from your docker-compose.yml to get internet
RUN mvn dependency:go-offline

# 2. Copy the rest of the source code
COPY src ./src

# 3. Build the application and skip tests
# This creates the .jar file in /app/target/
RUN mvn clean package -DskipTests

# ======================
# STAGE 2: The "Final Image"
# This stage is lightweight and secure for production.
# ======================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 4. Copy the built .jar file from the 'builder' stage
COPY --from=builder /app/target/*-SNAPSHOT.jar app.jar

# 5. Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]