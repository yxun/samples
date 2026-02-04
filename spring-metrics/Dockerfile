FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /app
COPY . /app

RUN mvn clean package -DskipTests

FROM openjdk:18-ea-slim

WORKDIR /app
COPY --from=builder /app/target/example-1.0-SNAPSHOT.jar app.jar

# metric port
EXPOSE 8084 8084

ENTRYPOINT ["java","-jar", "app.jar"]

