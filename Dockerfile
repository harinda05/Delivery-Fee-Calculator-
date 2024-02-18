# Builder stage
FROM gradle:8.5.0-jdk17 as builder
USER root
WORKDIR /builder
ADD . /builder
RUN gradle build --stacktrace

# Final stage
FROM gradle:8.5.0-jdk17
WORKDIR /app
EXPOSE 8080
COPY --from=builder /builder/build/libs/delivery-fee-calculator-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "delivery-fee-calculator-0.0.1-SNAPSHOT.jar"]
