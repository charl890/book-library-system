# Java Spring Boot REST API - Library Management System

[API documentation](docs/api-spec.md)

## JaCoCo Code Coverage

<img src="./Screenshot - JaCoCo Code Coverage.png" alt="JaCoCo code coverage" width="800" style="border-radius: 10px; border: 1px solid #e1e4e8;">

## Test UI

<img src="./Screenshot - Library Management System - Test UI.png" alt="Test UI" width="800" style="border-radius: 10px; border: 1px solid #e1e4e8;">

## Swagger Test

<img src="./Screenshot - Swagger Tests.png" alt="Swagger Tests" width="800" style="border-radius: 10px; border: 1px solid #e1e4e8;">

---

# Development Steps

## 1. Use Spring Initializr - start.spring.io

**Settings:**

- Project: Maven
- Java: 17
- Spring Boot: 3.5.5+

**Dependencies:**

- Spring Web
- Spring Data JPA
- Validation
- Lombok
- H2 and PostgreSQL Driver (manually add to `pom.xml`)

---

## 2. Folder structure

```
book-library-system/
│
├── Spring Boot app is backend with frontend as static HTML/js Test Page
├── Dockerfile and docker-compose.yml and docker\postgres\init.sql for config
├── docs/ # API docs, diagrams
├── README.md
```

---

## Spring Boot Java Backend folder structure

```
src/
├── main/
│   ├── java/com/test/book/library/system/
│   │
│   │   ├── config/ # Config classes (Swagger, CORS)
│   │   │   └── OpenApiConfig.java
│   │   │
│   │   ├── controller/ # REST Controllers
│   │   │   ├── BookController.java
│   │   │   ├── BorrowerController.java
│   │   │   └── BorrowRecordController.java
│   │   │
│   │   ├── service/ # Business logic
│   │   │   ├── BookService.java
│   │   │   ├── BorrowerService.java
│   │   │   └── BorrowService.java
│   │   │
│   │   ├── repository/ # JPA repositories
│   │   │   ├── BookRepository.java
│   │   │   ├── BorrowerRepository.java
│   │   │   └── BorrowRecordRepository.java
│   │   │
│   │   ├── entity/ # JPA entities
│   │   │   ├── Book.java
│   │   │   ├── Borrower.java
│   │   │   └── BorrowRecord.java
│   │   │
│   │   ├── dto/ # Request/Response DTOs
│   │   │   ├── BookRequest.java
│   │   │   ├── BorrowRequest.java
│   │   │   └── BorrowRecordResponse.java
│   │   │
│   │   ├── mapper/ # MapStruct / manual mappers
│   │   │   └── BookMapper.java
│   │   │
│   │   ├── exception/ # Global exception handling
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFoundException.java
│   │   │
│   │   └── LibrarySystemApplication.java
│   │
│   └── resources/
│       ├── application.yml
│       ├── data.sql # Optional seed data
│       ├── schema.sql # Optional schema
│       ├── application-dev.yml
│       ├── application-prod.yml
│       ├── logback.xml
│       └── static/
│           └── library.html # Test HTML/js page
│
└── test/java/ # Unit & integration tests

pom.xml
```

---

## Docker setup

```
docker-compose.yml
Dockerfile

```

---

## Test Frontend - HTML/js Page

```
src/main/resources/static/library.html # Main UI
```

---

## Docs folders

```
docs/
├── api-spec.md
├── architecture.png
├── database-schema.png
```

---

## Separation of concerns

- controller → API layer
- service → business logic
- repository → DB access
- entity → DB mapping
- dto → API contracts

---

## Scalable

Easy to add:

- Auth (`security/`)
- Caching (`cache/`)
- Events (`event/`)

---

## Clean Architecture Ready

You can evolve into:

```
controller → service → domain → repository
```

---

## 2. Create Dockerfile with java src and Add docker-compose.yml with postgres:15

---

## 3. Configure application.yml

- Use H2 for dev
- Switch to Postgres for production

---

## 4. Development Steps

- Add entities
- Add repositories
- Add services
- Add exceptions
- Add DTOs
- Add controllers
- Add test cases

---

## 5. Run the application

```bash
mvn clean install

mvn clean test
```

View Coverage Report:

```
target/site/jacoco/index.html
```

Run app (dev profile):

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Run with Docker (production):

```bash
docker compose up --build
```

---

## 6. Access URLs

- API Docs:
  [http://localhost:8080/api/v3/api-docs](http://localhost:8080/api/v3/api-docs)

- Test UI:
  [http://localhost:8080/api/library.html](http://localhost:8080/api/library.html)

- Swagger UI:
  [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

- [Postman collection](./LIBRARY_SYSTEM.postman_collection.json)

## 7. Stop Docker

```bash
docker compose down -v
```
