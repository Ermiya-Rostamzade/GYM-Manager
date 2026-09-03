# Gym Management Application

A backend application for managing the core operations of a gym, built with **Java** and **Spring Boot**.

The system is designed around the day-to-day workflow of a gym, including member management, subscription plans, payments, access tracking, locker management, and role-based permissions.

## Overview

The application provides a structured backend for managing gym members and the services they use.

The core domain covers:

* **Users** — Gym members and staff with different roles.
* **Plans** — Membership plans with different durations, prices, and types.
* **Subscriptions** — Memberships assigned to users based on selected plans.
* **Payments** — Payment records associated with subscriptions.
* **Traffic Logs** — Records of gym access, including check-in and check-out.
* **Lockers** — Physical lockers and their current availability.
* **Locker Reservations** — Locker assignments associated with users and their gym visits.
* **Roles & Permissions** — Role and permission management for controlling access.

The project is structured to keep responsibilities separated and provide a maintainable foundation for extending the system as the application grows.

## Tech Stack

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Spring Security**
* **Hibernate**
* **PostgreSQL**
* **Maven**

## Architecture

The application follows a layered architecture with clear separation between request handling, business logic, persistence, and domain representation.

```text
src/main/java/com/gym/management
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── mapper
├── repository
├── service
├── security
└── util
```

The architecture is organized around the following responsibilities:

* **API Layer** — Handles incoming HTTP requests and produces API responses.
* **Application Layer** — Coordinates business operations and enforces application rules.
* **Persistence Layer** — Handles database access and entity persistence.
* **Domain & Data Mapping** — Represents persistent data and separates internal entities from API contracts.
* **Cross-Cutting Concerns** — Provides shared infrastructure such as configuration, exception handling, security, and utilities.

This separation helps keep business logic independent from transport and persistence concerns while making individual components easier to maintain and test.

## Business Rules

The application models several rules around memberships, access, payments, and lockers.

### Users

* A user can have a defined role such as `ATHLETE`, `ADMIN`, or `RECEPTIONIST`.
* Users can be marked as active or inactive.
* A user can have multiple subscriptions over time.

### Membership Plans

* A plan defines a membership's **title, price, duration, and type**.
* Supported plan types currently include `MONTHLY`, `SESSIONAL`, and `VIP`.

### Subscriptions

* A subscription belongs to both a user and a membership plan.
* A subscription has a start date and an end date.
* A subscription tracks the user's remaining sessions where applicable.
* A subscription has a lifecycle status such as `PENDING`, `ACTIVE`, or `EXPIRED`.

### Payments

* Payments are associated with a specific subscription.
* A payment records its amount, reference code, status, and payment timestamp.
* Payment status distinguishes successful and failed transactions.

### Gym Access

* Each traffic log belongs to a user.
* A traffic log records both check-in and check-out activity.
* Access can be recorded through supported methods such as `QR_CODE`, `RFID`, or `FINGERPRINT`.

### Lockers

* Each locker has a unique locker number.
* Lockers are organized by gender section.
* A locker can be `EMPTY`, `OCCUPIED`, or under `MAINTENANCE`.
* Locker reservations associate a user with a specific locker during their gym visit.
* A reservation can be active or closed.
* A locker reservation can be linked to the traffic log that validates the user's presence.

### Roles & Permissions

* Users can have permissions associated with their account.
* Permissions are represented separately from the user entity, allowing access rules to evolve without tightly coupling them to the core user model.

## Core Services

The application is organized around dedicated services for each major domain:

| Service                    | Responsibility                                       |
| -------------------------- | ---------------------------------------------------- |
| `UserService`              | Manage users and their core information              |
| `PlanService`              | Manage gym membership plans                          |
| `UserSubscriptionService`  | Manage user subscriptions and their lifecycle        |
| `PaymentService`           | Manage payment records associated with subscriptions |
| `TrafficLogService`        | Track user check-in and check-out activity           |
| `LockerService`            | Manage lockers and their availability                |
| `LockerReservationService` | Manage locker assignments and reservations           |
| `RolePermissionService`    | Manage roles and user permissions                    |

## Getting Started

### Prerequisites

Make sure the following are available on your system:

* Java
* PostgreSQL
* Maven

### Database

Create a PostgreSQL database for the application.

Configure the required database environment variables in a `.env` file:

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
```

### Run the Application

Using the Maven Wrapper:

```powershell
.\mvnw clean spring-boot:run
```

Once the application starts, the backend will be available at:

```text
http://localhost:8080
```

For example, the plans management endpoint is available at:

```text
http://localhost:8080/admin/plans
```

## Project Status

This project is under active development.

The current implementation focuses on establishing the core domain model, business services, persistence layer, and API structure. Additional capabilities and infrastructure will be introduced incrementally as the application evolves.

## License

This project is licensed under the **MIT License**.

See the `LICENSE` file for the full license text.
