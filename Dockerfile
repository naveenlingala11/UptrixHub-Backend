# Use lightweight Java 21 runtime
FROM eclipse-temurin:21-jre

# App directory
WORKDIR /app

# Copy built jar
COPY target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Start app
CMD ["java", "-jar", "app.jar"]
