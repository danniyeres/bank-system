FROM alpine:latest
RUN apk add --no-cache openjdk17-jdk
WORKDIR /app
EXPOSE 8085
COPY target/notification-service-0.0.1-SNAPSHOT.jar notification-service-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "notification-service-0.0.1-SNAPSHOT.jar"]
