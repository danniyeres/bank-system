FROM alpine:latest
RUN apk add --no-cache openjdk17-jdk
WORKDIR /app
EXPOSE 8083
COPY target/account-service-0.0.1-SNAPSHOT.jar /app/account-service.jar
CMD ["java", "-jar", "account-service.jar"]