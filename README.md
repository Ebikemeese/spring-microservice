# E-Commerce Microservices

This repository contains a simple Spring Boot-based e-commerce microservice project with three independent services:

- `product-service` — product catalog and inventory-related functionality
- `user-service` — user management and account-related functionality
- `order-service` — order processing and order-related functionality

Each service runs as its own Spring Boot application and uses an embedded H2 database.

## Project Structure

```text
.
├── order-service/
│   ├── database/
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
├── product-service/
│   ├── database/
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
├── user-service/
│   ├── database/
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
└── README.md
```

## Tech Stack

- Java 17
- Spring Boot 4.1.1
- Maven
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Lombok

## Prerequisites

Before running the services, make sure you have:

- JDK 17 or later
- Maven or the included Maven wrapper (`mvnw`)
- A terminal/command prompt

## Running the Services

Each service can be started independently.

### 1. Product Service

```bash
cd product-service
./mvnw spring-boot:run
```

Runs on:

- http://localhost:8081

### 2. User Service

```bash
cd user-service
./mvnw spring-boot:run
```

Runs on:

- http://localhost:8082

### 3. Order Service

```bash
cd order-service
./mvnw spring-boot:run
```

Runs on:

- http://localhost:8083

## H2 Database Console

Each service has the H2 console enabled. You can access it through the browser using the service port:

- Product Service: http://localhost:8081/h2-console
- User Service: http://localhost:8082/h2-console
- Order Service: http://localhost:8083/h2-console

Use the following connection settings (same across services):

- JDBC URL: `jdbc:h2:file:./database/ecomdb`
- Username: `sa`
- Password: `password`

## Notes

- This project is structured as a multi-service application and is intended to be extended with inter-service communication, API gateways, or service discovery in the future.
- The services currently share a similar configuration and are designed to be developed independently.

## Useful Commands

Build a service:

```bash
./mvnw clean package
```

Run tests:

```bash
./mvnw test
```
