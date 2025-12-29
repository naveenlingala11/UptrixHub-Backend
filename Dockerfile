FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar (Spring Boot fat jar)
COPY target/*.jar app.jar

# Expose port for Koyeb
EXPOSE 8080

# IMPORTANT: Bind to PORT env (Koyeb injects PORT)
ENTRYPOINT ["java", "-jar", "app.jar"]
