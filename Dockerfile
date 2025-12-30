# =========================
# 1️⃣ Build stage
# =========================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

# Copy Maven wrapper & pom
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw

# Give execute permission
RUN chmod +x mvnw

# Download dependencies (cache layer)
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copy source
COPY src src

# Build fat jar
RUN ./mvnw clean package -DskipTests


# =========================
# 2️⃣ Runtime stage (🔥 MUST BE JDK)
# =========================
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /build/target/*SNAPSHOT.jar app.jar

EXPOSE 8080

# Optional safety limits
ENV JAVA_TOOL_OPTIONS="-Xms64m -Xmx256m"

ENTRYPOINT ["java","-jar","app.jar"]
