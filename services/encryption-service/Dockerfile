# Use an official OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Package the application using Maven
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Expose the default Spring Boot port
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "target/encryption-service-0.0.1-SNAPSHOT.jar"]