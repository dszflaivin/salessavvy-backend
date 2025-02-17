# Sales Savvy - Backend

## Description
Sales Savvy's backend is built with **Spring Boot**. It provides an API to manage sales data, users, and products. The backend handles authentication, database interactions, and business logic.

## Features
- **User Authentication**: JWT-based authentication for secure access.
- **Product Management**: CRUD operations for managing products.
- **Sales Management**: Endpoints for tracking and managing sales.
- **Database Integration**: Uses **MySQL** (or other databases as needed).

## Technologies Used
- **Spring Boot** (for building the backend application)
- **Spring Data JPA** (for database access)
- **Spring Security** (for authentication and authorization)
- **JWT** (for token-based authentication)
- **MySQL** (or any other database)
- **Maven** (for dependency management)

## Installation

### Prerequisites
- **JDK** (version 11 or higher)
- **Maven**
- **MySQL** (or other database)

### Steps to Install
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/sales-savvy-backend.git

2. Navigate to the project directory:
  cd sales-savvy-backend

3. Configure your application.properties file for your database:
 ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/database_name
  spring.datasource.username=your-username
  spring.datasource.password=your-password
```
4. Build and run the application:
  ```bash
  mvn spring-boot:run
```
The backend will be running at http://localhost:8080.
