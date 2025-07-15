# Use an official OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the entire project source into the container
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project and package, skipping tests for faster build
RUN ./mvnw clean package -DskipTests

# Expose the default Spring Boot port
EXPOSE 8080

# Run the built JAR file
CMD ["java", "-jar", "target/blockchain-service-0.0.1-SNAPSHOT.jar"]
