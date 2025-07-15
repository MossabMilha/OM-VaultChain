# Use an official OpenJDK base image
FROM eclipse-temurin:17-jdk-jammy

# Metadata
LABEL maintainer="OM VaultChain Team"
LABEL description="Blockchain microservice for OM VaultChain"

# Set workdir
WORKDIR /app

# Copy built jar file
COPY target/blockchain-service.jar app.jar

# Expose port (optional, for debugging)
EXPOSE 8080

# Default environment profile (optional)
ENV SPRING_PROFILES_ACTIVE=prod

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
