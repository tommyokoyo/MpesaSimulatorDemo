FROM openjdk:21-jdk-slim
LABEL authors="tommyokoyo"

WORKDIR /app

COPY ./MpesaSimulatorDemo-0.0.1-SNAPSHOT.jar /app/mpesasimulator.jar

# Expose the port your application runs on
EXPOSE 9095

# Set the command to run your application
CMD ["java", "-jar", "/app/mpesasimulator.jar"]
