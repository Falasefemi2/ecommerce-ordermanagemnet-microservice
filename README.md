# E-Commerce Order Management Microservices

## Overview

The primary goal of this project is to showcase a deep understanding of distributed systems, cloud-native principles, and the Java Spring ecosystem. It serves as a practical example of how to build, deploy, and manage a complex application composed of multiple independent services.

## Key Features

*   **Decoupled Services:** Each business capability (Product, Order, Payment) is isolated in its own microservice.
*   **Centralized Entry Point:** A single API Gateway routes all external requests to the appropriate internal service, providing a unified interface for clients.
*   **Dynamic Service Discovery:** Services register with a discovery server, allowing them to locate and communicate with each other dynamically without hardcoded addresses.
*   **Fault Tolerance and Resilience:** The architecture is designed to be resilient. The failure of one service (e.g., Payment Service) will not cascade and bring down the entire system.
*   **Scalability:** Individual services can be scaled independently based on demand, optimizing resource utilization.
*   **Maintainability:** Smaller, focused codebases for each service make them easier to understand, develop, and deploy.

## System Architecture

The system is composed of the following core microservices:

1.  **API Gateway (`api-gateway`):**
    *   The single entry point for all client requests.
    *   It handles request routing, load balancing, and security.
    *   Built with Spring Cloud Gateway.

2.  **Discovery Service (`discovery-service`):**
    *   Acts as a service registry where all other microservices register themselves.
    *   Allows services to find each other by a logical service name rather than a physical IP address.
    *   Built with Spring Cloud Netflix Eureka.

3.  **Product Service (`product-service`):**
    *   Manages the product catalog, including creating, reading, updating, and deleting products (CRUD).
    *   Maintains product information such as name, description, price, and stock levels.

4.  **Order Service (`order-service`):**
    *   Handles all operations related to customer orders.
    *   Responsible for creating new orders, retrieving order history, and managing order statuses.
    *   Communicates with the Product Service to verify product availability and with the Payment Service to process payments.

5.  **Payment Service (`payment-service`):**
    *   Processes payments for orders.
    *   This service simulates interactions with a third-party payment provider.

### Communication Flow
1. A client sends a request (e.g., to place an order) to the **API Gateway**.
2. The **API Gateway** looks up the address of the target service (e.g., `order-service`) from the **Discovery Service**.
3. The Gateway forwards the request to the appropriate service instance.
4. If needed, the `order-service` communicates with other services (like `product-service`) by looking them up via the **Discovery Service**.
5. Each service operates on its own database, ensuring loose coupling.

## Technologies Used

*   **Backend:** Java 17, Spring Boot 3
*   **Microservices Framework:** Spring Cloud
    *   **API Gateway:** Spring Cloud Gateway
    *   **Service Discovery:** Spring Cloud Netflix Eureka
    *   **Inter-service Communication:** RestTemplate / OpenFeign
*   **Data Persistence:** Spring Data JPA, H2 Database (for development), PostgreSQL (for production)
*   **Build & Dependency Management:** Apache Maven
*   **Architectural Patterns:** Microservices, API Gateway, Service Discovery, Database-per-Service

## Getting Started

### Prerequisites

*   Java Development Kit (JDK) 17 or later
*   Apache Maven 3.8.x or later
*   Git

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Falasefemi2/ecommerce-ordermanagemnet-microservice.git
    cd E-CommerceOrderManagementMicroservices
    ```

2.  **Build all microservices:**
    Each service is a standalone Maven project. You can build them individually or use a script to build all of them. For example, to build the `product-service`:
    ```bash
    cd product-service
    mvn clean install
    ```

### Running the Application

The services must be started in a specific order to ensure proper registration and discovery.

1.  **Start the Discovery Service:**
    ```bash
    cd discovery-service
    mvn spring-boot:run
    ```
    Wait for it to start completely. You can check the Eureka dashboard at `http://localhost:8761`.

2.  **Start the API Gateway:**
    ```bash
    cd api-gateway
    mvn spring-boot:run
    ```

3.  **Start the Core Services:**
    Start the `product-service`, `order-service`, and `payment-service` in any order.
    ```bash
    # In a new terminal
    cd product-service
    mvn spring-boot:run

    # In another new terminal
    cd order-service
    mvn spring-boot:run

    # And so on...
    ```

## API Endpoints

All endpoints are accessed through the API Gateway, which typically runs on port `8080`. The gateway prefixes service routes with the service name.

### Product Service (`/api/products`)

*   `GET /api/products`: Retrieves a list of all products.
*   `GET /api/products/{id}`: Retrieves a single product by its ID.
*   `POST /api/products`: Creates a new product.
*   `PUT /api/products/{id}`: Updates an existing product.

### Order Service (`/api/orders`)

*   `POST /api/orders`: Places a new order. The request body should contain product IDs and quantities.
*   `GET /api/orders/{id}`: Retrieves the details of a specific order.

### Payment Service (`/api/payments`)

*   `POST /api/payments`: Processes a payment for an order.

## Future Improvements

*   **Security:** Implement authentication and authorization using Spring Security and OAuth2/JWT to secure endpoints.
*   **Containerization:** Create `Dockerfile` for each service and a `docker-compose.yml` file to orchestrate the entire application stack.
*   **Configuration Management:** Externalize configuration using a Config Server (e.g., Spring Cloud Config) to manage properties for all services in a central location.
*   **Resilience:** Integrate a circuit breaker pattern (e.g., Resilience4j) to prevent cascading failures.
*   **Testing:** Add comprehensive integration and contract tests to ensure reliability between services.
*   **CI/CD:** Set up a continuous integration and deployment pipeline using tools like Jenkins or GitHub Actions.
