FROM alpine:latest
RUN apk add --no-cache openjdk17-jdk
WORKDIR /app
EXPOSE 8080
COPY target/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar
CMD ["java", "-jar", "api-gateway.jar"]
