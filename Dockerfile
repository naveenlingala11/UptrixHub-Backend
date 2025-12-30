FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy Spring Boot fat jar
COPY target/*.jar app.jar

# Render injects PORT automatically
EXPOSE 8080

# IMPORTANT: bind Spring Boot to PORT env
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
