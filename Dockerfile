FROM openjdk:17-jdk-slim
LABEL authors="lcy"
WORKDIR /app
COPY . .
RUN chmod +x gradlew
ENTRYPOINT ["java", "-jar", "build/libs/loadbalancer-0.0.1-SNAPSHOT.jar"]