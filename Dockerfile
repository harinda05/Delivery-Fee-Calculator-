# Builder stage
FROM eclipse-temurin:17-jdk-alpine as builder
USER root
WORKDIR /builder
ADD . /builder
RUN gradle build --stacktrace

# Final stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=builder /builder/build/libs/delivery-fee-calculator-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "delivery-fee-calculator-0.0.1-SNAPSHOT.jar"]
