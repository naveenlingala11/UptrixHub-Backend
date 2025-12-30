# =========================
# 1️⃣ Build stage
# =========================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

# Copy Maven wrapper & pom
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw

# 🔥 FIX: give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copy source
COPY src src

# Build fat jar
RUN ./mvnw clean package -DskipTests


# =========================
# 2️⃣ Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /build/target/*SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
