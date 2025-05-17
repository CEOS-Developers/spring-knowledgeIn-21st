# build stage
FROM eclipse-temurin:21 AS builder
ENV GRADLE_OPTS="-Xmx2g"
WORKDIR /app

COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY . .

RUN ./gradlew clean build -x test --no-daemon

# run stage
FROM amazoncorretto:21

WORKDIR /app

EXPOSE 8080

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.profiles.active=docker"]