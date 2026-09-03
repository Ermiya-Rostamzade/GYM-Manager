# 🏋️‍♂️ Gym Management Application

<p align="center">
  <strong>A robust, enterprise-ready backend system for streamlining gym operations and automated facility workflows.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" alt="Hibernate" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License" />
</p>

---

## 📌 Overview

A backend application for managing the core operations of a gym, built with **Java** and **Spring Boot**.

The system is designed around the day-to-day workflow of a gym, including member management, subscription plans, payments, access tracking, locker management, and role-based permissions.

The application provides a structured backend for managing gym members and the services they use.

### 🧩 Core Domain Model
* 👤 **Users** — Gym members and staff with different roles.
* 📋 **Plans** — Membership plans with different durations, prices, and types.
* 🎫 **Subscriptions** — Memberships assigned to users based on selected plans.
* 💳 **Payments** — Payment records associated with subscriptions.
* ⏱️ **Traffic Logs** — Records of gym access, including check-in and check-out.
* 🔐 **Lockers** — Physical lockers and their current availability.
* 🔑 **Locker Reservations** — Locker assignments associated with users and their gym visits.
* 🛡️ **Roles & Permissions** — Role and permission management for controlling access.

The project is structured to keep responsibilities separated and provide a maintainable foundation for extending the system as the application grows.

---

## 🛠️ Tech Stack

| Technology | Role / Usage |
| :--- | :--- |
| **Java** | Primary programming language (17+) |
| **Spring Boot** | Application framework & dependency injection container |
| **Spring Data JPA** | Simplified data access abstraction layer |
| **Spring Security** | Authentication, authorization, and endpoint protection |
| **Hibernate** | Object-Relational Mapping (ORM) engine |
| **PostgreSQL** | Relational database storage |
| **Maven** | Build management and dependency resolution |

---

## 🏗️ Architecture

The application follows a layered architecture with clear separation between request handling, business logic, persistence, and domain representation.

```text
src/main/java/com/gym/management
│
├── config                 # Global configurations & framework setups
├── controller             # REST & MVC endpoints handling HTTP traffic
├── dto                    # Data Transfer Objects
│   ├── request            # Client-to-server payload models
│   └── response           # Structured server-to-client responses
├── entity                 # Database persistence models (JPA Entities)
├── exception              # Centralized error handling & custom exceptions
├── mapper                 # Object mapping contracts (Entity <-> DTO)
├── repository             # Data access interfaces extending Spring Data JPA
├── service                # Core business rules and domain logic orchestration
├── security               # Security filters, tokens, and role definitions
└── util                   # Shared helper utilities and common constants
```

### 📐 Structural Responsibilities
* **API Layer** — Handles incoming HTTP requests and produces API responses.
* **Application Layer** — Coordinates business operations and enforces application rules.
* **Persistence Layer** — Handles database access and entity persistence.
* **Domain & Data Mapping** — Represents persistent data and separates internal entities from API contracts.
* **Cross-Cutting Concerns** — Provides shared infrastructure such as configuration, exception handling, security, and utilities.

> This separation helps keep business logic independent from transport and persistence concerns while making individual components easier to maintain and test.

---

## ⚖️ Business Rules

The application models several rules around memberships, access, payments, and lockers:

### 👤 Users
* A user can have a defined role such as `ATHLETE`, `ADMIN`, or `RECEPTIONIST`.
* Users can be marked as active or inactive.
* A user can have multiple subscriptions over time.

### 📋 Membership Plans
* A plan defines a membership's **title, price, duration, and type**.
* Supported plan types currently include `MONTHLY`, `SESSIONAL`, and `VIP`.

### 🎫 Subscriptions
* A subscription belongs to both a user and a membership plan.
* A subscription has a start date and an end date.
* A subscription tracks the user's remaining sessions where applicable.
* A subscription has a lifecycle status such as `PENDING`, `ACTIVE`, or `EXPIRED`.

### 💳 Payments
* Payments are associated with a specific subscription.
* A payment records its amount, reference code, status, and payment timestamp.
* Payment status distinguishes successful and failed transactions.

### 🚪 Gym Access
* Each traffic log belongs to a user.
* A traffic log records both check-in and check-out activity.
* Access can be recorded through supported methods such as `QR_CODE`, `RFID`, or `FINGERPRINT`.

### 🔐 Lockers
* Each locker has a unique locker number.
* Lockers are organized by gender section.
* A locker can be `EMPTY`, `OCCUPIED`, or under `MAINTENANCE`.
* Locker reservations associate a user with a specific locker during their gym visit.
* A reservation can be active or closed.
* A locker reservation can be linked to the traffic log that validates the user's presence.

### 🛡️ Roles & Permissions
* Users can have permissions associated with their account.
* Permissions are represented separately from the user entity, allowing access rules to evolve without tightly coupling them to the core user model.

---

## ⚙️ Core Services

The application is organized around dedicated services for each major domain:

| Service | Responsibility |
| :--- | :--- |
| `UserService` | Manage users and their core information |
| `PlanService` | Manage gym membership plans |
| `UserSubscriptionService` | Manage user subscriptions and their lifecycle |
| `PaymentService` | Manage payment records associated with subscriptions |
| `TrafficLogService` | Track user check-in and check-out activity |
| `LockerService` | Manage lockers and their availability |
| `LockerReservationService` | Manage locker assignments and reservations |
| `RolePermissionService` | Manage roles and user permissions |

---

## 🚀 Getting Started

### Prerequisites

Make sure the following tools are installed and configured on your system:
* **Java** (v17 or higher)
* **PostgreSQL** (v14+ recommended)
* **Maven** (or use the included Maven Wrapper)

### 🗄️ Database Configuration

1. Create a PostgreSQL database for the application.
2. Configure the required database environment variables in a `.env` file (or application properties):

```env
DB_URL=jdbc:postgresql://localhost:5432/gym_db
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

### 🏃 Run the Application

Using the Maven Wrapper:

* **Windows (PowerShell):**
  ```powershell
  .\mvnw clean spring-boot:run
  ```
* **Linux / macOS:**
  ```bash
  ./mvnw clean spring-boot:run
  ```

Once the application starts, the backend will be available at:
```text
http://localhost:8080
```

For example, the plans management endpoint is available at:
```text
http://localhost:8080/admin/plans
```

---

## 📈 Project Status

> **Note:** This project is under active development.

The current implementation focuses on establishing the core domain model, business services, persistence layer, and API structure. Additional capabilities and infrastructure will be introduced incrementally as the application evolves.

---

## 📄 License

This project is licensed under the **MIT License**.

See the `LICENSE` file for the full license text.
