FROM gradle:8.10-jdk21 AS build
WORKDIR /workspace
COPY --chown=gradle:gradle settings.gradle build.gradle ./
COPY --chown=gradle:gradle services/api/build.gradle services/api/build.gradle
COPY --chown=gradle:gradle services/api/src services/api/src
RUN gradle :services:api:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/services/api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
