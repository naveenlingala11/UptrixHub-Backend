# =========================
# 1️⃣ Build stage
# =========================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

# Copy only pom first (cache optimization)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
RUN ./mvnw -q -e -DskipTests dependency:go-offline

# Copy source
COPY src src

# Build fat jar
RUN ./mvnw clean package -DskipTests


# =========================
# 2️⃣ Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=builder /build/target/*SNAPSHOT.jar app.jar

# Render injects PORT automatically
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-jar","app.jar"]
