# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests -Dspring.profiles.active=container

# Production runtime stage
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app
COPY ./config /app/config
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "-Djavax.net.debug=ssl:handshake"]

