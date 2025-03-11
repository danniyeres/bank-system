# Banking System Project

## Overview

The Banking System Project is a microservices-based application designed to handle various banking operations. It is built using Java, Spring Boot, and Maven, and it leverages Docker for containerization and orchestration.

## Modules

The project consists of the following modules:

- **api-gateway**: Acts as the entry point for all client requests and routes them to the appropriate services.
- **auth-service**: Handles authentication and authorization.
- **user-service**: Manages user information and operations.
- **notification-service**: Sends notifications to users.
- **account-service**: Manages bank accounts and related operations.
- **transaction-service**: Handles transactions between accounts.

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/danniyeres/bank-system.git
cd banking-system