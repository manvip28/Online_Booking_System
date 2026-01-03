# Online Event & Ticket Booking System

A production-grade, Spring Boot-based backend for an online movie/event ticket booking system (similar to BookMyShow). This project demonstrates clean architecture, scalable design, and the use of modern tools like Redis, Kafka, and Docker.

## 🚀 Features

*   **Authentication & Authorization**: Secure JWT-based authentication with Role-Based Access Control (RBAC) only allowing Admins to manage events/shows.
*   **Concurrency Control**: Handling multiple users trying to book the same seat simultaneously using **Pessimistic Locking** on the database.
*   **Caching**: High-performance caching of Event and Show data using **Redis** to reduce database load.
*   **Asynchronous Messaging**: Decoupled booking notifications using **Kafka** (Producer-Consumer model).
*   **Database**: Relational data modelling with **PostgreSQL**.
*   **Containerization**: Fully Dockerized environment using **Docker Compose**.
*   **API Documentation**: Integrated **Swagger UI / OpenAPI** for easy API exploration.
*   **Input Validation**: Strict validation using Jakarta Validation API.

## 🛠️ Tech Stack

*   **Language**: Java 17
*   **Framework**: Spring Boot 3.2.0
*   **Database**: PostgreSQL
*   **Cache**: Redis
*   **Message Broker**: Apache Kafka
*   **Security**: Spring Security, JWT (JJWT), BCrypt
*   **Build Tool**: Maven
*   **DevOps**: Docker, Docker Compose
*   **Testing**: JUnit 5, Mockito, Testcontainers

## ⚙️ Prerequisites

*   **Docker Desktop** (must be running)
*   **Java 17+** (optional, only if running locally without Docker)
*   **Maven** (optional, only if running locally without Docker)

## 🏃‍♂️ Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd OnlineBookingSystem
```

### 2. Configuration (.env)
The project uses a `.env` file for secrets. A default one is included for development:
```env
DB_USER=my_secure_user
DB_PASS=my_super_secret_password
...
```

### 3. Run with Docker Compose (Recommended)
This command will build the application and start Postgres, Redis, Kafka, and the App services.
```bash
docker-compose up --build
```
*   **App URL**: `http://localhost:8080`
*   **Swagger API Docs**: `http://localhost:8080/swagger-ui/index.html`

### 4. Data Seeding
On the first run, the application automatically seeds the database with:
*   **Admin User**: `admin@test.com` / `password`
*   **Normal User**: `user@test.com` / `password`
*   **Event**: "Avengers: Secret Wars"
*   **Venue**: "PVR Cinemas, Mumbai"
*   **Show**: Tomorrow at 6:00 PM
*   **Seats**: A1 to A10 (Screen 1)

## 🧪 Testing
The project includes Unit Tests and Integration Tests (using Testcontainers).
To run tests (ensure Docker is running):
```bash
mvn test
```

## 🔌 API Endpoints

### Authentication
*   `POST /auth/register` - Register a new user
*   `POST /auth/login` - Login and get JWT Token

### Events (Public)
*   `GET /events` - List all events (Cached)
*   `GET /events/{id}` - Get event details
*   `GET /events/{id}/shows` - Get shows for an event

### Admin (Requires Admin JWT)
*   `POST /admin/events` - Create Event (Invalidates Cache)
*   `POST /admin/shows` - Create Show
*   `GET /bookings` - View all bookings

### Bookings (Requires User JWT)
*   `POST /bookings` - Book seats
    *   Body: `{"showId": 1, "seatIds": [1, 2]}`
*   `GET /bookings/{id}` - Get booking details

## 📂 Project Structure
```
src/main/java/com/app/booking
├── config/             # Security, OpenAPI, App Configurations
├── controller/         # REST Controllers
├── dto/                # Data Transfer Objects (Request/Response)
├── entity/             # JPA Entities
├── exception/          # Global Exception Handling
├── repository/         # JPA Repositories
└── service/            # Business Logic (Caching, Transactions)
```
