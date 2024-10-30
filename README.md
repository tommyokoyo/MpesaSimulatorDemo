
# M-Pesa Daraja Offline Simulator

## Project Overview
This project is a **replica of the M-Pesa Daraja APIs** but designed to run in an **offline environment**. It serves as a learning tool to understand how **microservices communicate** with each other and provides a local, offline version of the M-Pesa Daraja APIs for **testing purposes**.

### Key Features:
- **Offline M-Pesa API simulator** for local testing
- **Microservice architecture** with two queues:
    - **Transaction Queue**: Handles incoming transactions.
    - **Callback Queue**: Sends callbacks for transaction responses.
- **BlockingQueue** is used to manage and run the queues, simulating real-world transaction workflows.

This application helps you to:
- Learn microservice communication patterns (when Included with the C2B service api).
- Simulate **M-Pesa Daraja API** interactions in your local environment.
- Test API integrations without requiring access to the actual M-Pesa API.

## Installation Instructions
The API is built with **Spring Boot** and is containerized into a **Docker image** for easy setup across different systems. To run the simulator, follow these steps:

1. **Pull the Docker image**:
   ```bash
   docker pull tommyokoyo/mpesasimulator
   ```
2. **Run the image** on port `9095`:
   ```bash
   docker run -p 9095:9095 tommyokoyo/mpesasimulator
   ```
3. **Swagger Documentation** is available to guide you through the available endpoints and how to interact with the API. Once the container is running, you can access the Swagger UI at:
   ```
   http://localhost:9095/swagger-ui.html
   ```

## Technologies Used
- **Java** for the core application.
- **Spring Boot** for building the API.
- **Docker** for containerization and easy deployment.
- **GitHub Actions** for CI/CD and automating Docker builds and deployments.
