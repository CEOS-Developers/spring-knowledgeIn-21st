# => 이렇게 하니까 image용량이 1/4로 줄음(884MB -> 229MB)
#############################################
# 1) Build Stage – Gradle + JDK17 (Alpine)  #
#############################################
FROM gradle:8.6-jdk17-alpine AS builder
WORKDIR /workspace

# (A) gradle 파일을 Image에 복사 & 의존성 다운
COPY settings.gradle build.gradle gradlew* /workspace/
COPY gradle /workspace/gradle
RUN ./gradlew dependencies --no-daemon      # 의존성만 다운(나중에 의존성 변겨 없으면 캐싱 가능)

# (B) 애플리케이션 소스 복사 후 빌드
COPY src /workspace/src
RUN ./gradlew clean bootJar --no-daemon # jar파일 생성

#############################################
# 2) Run Stage – JDK17 (Alpine)             #
#############################################
FROM eclipse-temurin:17-alpine
WORKDIR /app

# 빌드 산출물만 복사
COPY --from=builder /workspace/build/libs/*.jar app.jar # build stage에서 생성한 jar파일 복사

# 실행 시 필요한 환경 변수
ENV SPRING_PROFILES_ACTIVE=docker \
    JWT_SECRET_KEY=4iLaz4RBs9F0g1WAVppByh0uH/oeT6yT0zWzLx8fGPE \
    JWT_REFRESH_TOKEN_EXPIRATION=86400000 \
    JWT_ACCESS_TOKEN_EXPIRATION=60000

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"] # 실행 명령어

