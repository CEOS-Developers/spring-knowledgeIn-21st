# 1단계: 빌드
FROM gradle:7.6-jdk17 AS builder
WORKDIR /build
COPY . .
RUN gradle build -x test

# 2단계: 실행
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]