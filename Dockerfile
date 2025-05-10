FROM openjdk:21-jdk-slim

# JAR 복사 및 실행
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
