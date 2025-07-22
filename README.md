# Pharma Supply Chain and Prescription Fulfillment System

## Overview

This project is a **Pharma Supply Chain and Prescription Fulfillment System** designed for a pharmaceutical company. It manages the inventory of drugs, prescriptions, and pharmacies, as well as the logistics of fulfilling patient prescriptions. The system is built using **Spring Boot** and follows **DDD (Domain-Driven Design)** and **Clean Architecture** principles.

## Features

- **Drug Management**: Add drugs to the inventory, manage stock, and ensure drugs are dispensed only if they are within their expiry date.
- **Pharmacy Management**: Create, modify, and close pharmacies. Manage pharmacy drug allocations with allocation limits.
- **Prescription Management**: Submit and fulfill prescriptions, ensuring all requested drugs are available and within allocation limits.
- **Audit Logs**: Retrieve audit logs with filters for patient, pharmacy, or success/failure.

## Architecture

The project is structured using **DDD + Clean Architecture**:

```
-adapter
    -persistence
        -adapter
        -entity
        -mapper
        -repository 
    -web
        -adapter
        -advice
        -controller
        -dto
        -exception
        -mapper
        -security
-application
    -service
-domain
    -model
    -repository
```

## Technologies Used

- **Languages**: Java, Kotlin
- **Frameworks**: Spring Boot, Spring WebFlux, Spring Data R2DBC
- **Database**: PostgreSQL with R2DBC
- **Migrations**: Flyway
- **Testing**: JUnit 5, Mockito, AssertJ
- **Build Tool**: Gradle (Kotlin DSL)
- **Other Libraries**:
  - MapStruct (for mapping)
  - Lombok (for boilerplate code reduction)
  - SpringDoc OpenAPI (for API documentation)
  - UUID Creator (for unique identifiers)

## Prerequisites

- Java 17
- PostgreSQL
- Gradle

## Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. Configure the database in `application.yml`:
   ```yaml
   spring:
     r2dbc:
       url: r2dbc:postgresql://<host>:<port>/<database>
       username: <username>
       password: <password>
   ```

3. Run database migrations:
   ```bash
   ./gradlew flywayMigrate
   ```

4. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```

## API Endpoints

### Drug Management

- **Add a Drug**: Add a drug to the inventory, specifying its details and stock.
    - Notes:
        - Drugs with the same `manufacturer` and `batch number` must have the same `expiry date`.
        - Adding a drug with the same `manufacturer` and `batch number` updates the existing stock.

### Pharmacy Management

- **List Pharmacies**: Retrieve all pharmacies and their contracted drugs.
- **Manage Pharmacy**:
    - Create, modify, or close a pharmacy.
    - Add or update `pharmacy_drug_allocation` with allocation limits.
    - Ensure the total allocation limit across all pharmacies does not exceed the total available stock of non-expired drugs.

### Prescription Management

- **Create a Prescription**: Submit a prescription for a patient at a specific pharmacy.
- **Fulfill a Prescription**:
    - Validate the prescription:
        - All requested drugs must be available and within allocation limits.
        - If any drug is unavailable, the prescription fails with an error.
    - Update stock levels for dispensed drugs.

### Audit Logs

- Retrieve audit logs with filters for:
    - Patient
    - Pharmacy
    - Success/Failure

## Testing

Run tests using:
```bash
./gradlew test
```

## Code Quality

- **Spotless**: Ensures consistent code formatting.
- **Jacoco**: Generates test coverage reports.
    - Minimum coverage threshold: 70%
    - Excluded packages: `entity`, `request`, `mapper`, `model`, `config`

## License

This project is licensed under the MIT License.
```
