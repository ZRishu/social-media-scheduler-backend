# --- Stage 1: Build the application ---
# This stage uses a full Maven and JDK image to build your application's JAR file.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first to leverage Docker's layer caching
COPY pom.xml .

# Copy the rest of your source code
COPY src ./src

# Run the Maven package command to build the JAR file
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final, lightweight image ---
# This stage starts from a minimal Java Runtime (JRE) image
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy only the compiled JAR file from the 'build' stage into the final image
COPY --from=build /app/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on inside the container
EXPOSE 9000

# This is the command that will be run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]