# Build stage
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the correct JAR file
COPY --from=builder /app/target/presentation-service-*.jar /app/app.jar
COPY config.yml .

# Create non-root user
RUN useradd -m appuser && chown -R appuser /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["server", "config.yml"]