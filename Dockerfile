# Final stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
EXPOSE 8080
COPY /build/libs/delivery-fee-calculator-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "delivery-fee-calculator-0.0.1-SNAPSHOT.jar"]
