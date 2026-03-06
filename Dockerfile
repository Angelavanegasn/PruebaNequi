FROM gradle:8.14.4-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle :applications:app-service:clean :applications:app-service:bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/applications/app-service/build/libs/*.jar app.jar

ENV SERVER_PORT=8080
ENV SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/reto_angela

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
