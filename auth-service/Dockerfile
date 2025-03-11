FROM alpine:latest
RUN apk add --no-cache openjdk17-jdk
WORKDIR /app
EXPOSE 8082
COPY target/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar
CMD ["java", "-jar", "auth-service.jar"]