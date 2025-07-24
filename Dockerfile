FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build and run
RUN ./mvnw clean install -DskipTests
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]