FROM alpine:latest
RUN apk add --no-cache openjdk17-jdk
WORKDIR /app
EXPOSE 8084
COPY target/transaction-service-0.0.1-SNAPSHOT.jar /app/transaction-service.jar
CMD ["java", "-jar", "transaction-service.jar"]