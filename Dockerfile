# 🏗️ Stage 1: Build the application
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app

# Install Maven to build the project
RUN apt-get update && apt-get install -y maven

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and compile the application
COPY src ./src
RUN mvn clean package -DskipTests

# 🏗️ Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar /app/smart-pot-backend.jar

# Copy the .env file from the build context into the container
COPY .env /app/.env

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/smart-pot-backend.jar"]
