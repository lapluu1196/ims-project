# Use official OpenJDK 21 runtime as base image
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY ims-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (Spring Boot mặc định chạy trên 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]