# Library Management REST API

A lightweight, production-ready **Spring Boot REST API** for managing a simple library system.

Supports:

- Book registration (multiple copies allowed)
- Borrower management
- Borrow / Return workflow
- Active borrow tracking

---

# Base URL

```bash
http://localhost:8080/v1/library
```

# Test UI

```bash
http://localhost:8080/api/library.html
```

# Swagger UI

```bash
http://localhost:8080/api/swagger-ui.html
```

---

# API Overview

| Resource  | Endpoint     |
| --------- | ------------ |
| Books     | `/books`     |
| Borrowers | `/borrowers` |
| Borrows   | `/borrows`   |

---

# 1. Book APIs

## Register a Book

**POST** `/books`

### Request

```json
{
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch"
}
```

### Response (201 Created)

```json
{
  "id": "uuid",
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch"
}
```

### Behavior

- Allows **multiple copies** of same ISBN
- Validates:
  - Same ISBN → must have same title & author

- Creates a new **book copy (new ID)**

---

## Get All Books

**GET** `/books`

---

## Get Available Books

**GET** `/books/available`

Returns books that are **NOT currently borrowed**

---

# 2. Borrower APIs

## Register Borrower

**POST** `/borrowers`

### Request

```json
{
  "name": "Alice Johnson",
  "email": "alice@example.com"
}
```

### Response (201 Created)

```json
{
  "id": "uuid",
  "name": "Alice Johnson",
  "email": "alice@example.com"
}
```

---

## Get All Borrowers

**GET** `/borrowers`

---

# 3. Borrow APIs

## Borrow a Book

**POST** `/borrows`

### Request

```json
{
  "borrowerId": "uuid",
  "bookId": "uuid",
  "borrowMsg": "For reading"
}
```

### Response (201 Created)

```json
{
  "recordId": "uuid",
  "bookId": "uuid",
  "borrowerId": "uuid",
  "borrowedAt": "2026-03-26T10:00:00",
  "returnedAt": null
}
```

### Rules

- A **book copy can only be borrowed by ONE borrower at a time**
- System enforces:

  ```text
  returnedAt == null → book is currently borrowed
  ```

---

## Return a Book

**POST** `/borrows/{id}/return`

### Example

```bash
POST /borrows/123e4567-e89b-12d3-a456-426614174000/return
```

### Response

```json
{
  "recordId": "uuid",
  "returnedAt": "2026-03-26T12:00:00"
}
```

---

## Get Active Borrows

**GET** `/borrows/active`

Returns all currently borrowed books

---

# static HTML/js Client

The provided UI supports:

### Book List

- Displays all registered books
- Dropdown used for borrowing

### Regster New Book

### Borrower List

- Displays all borrowers
- Dropdown used for assignment

### Register New Borrower

### Assign Book to Borrower

- Select:
  - Book
  - Borrower

- Click **Assign**
- Calls:

  ```bash
  POST /borrows
  ```

---

### Current Borrowed Books

- Displays active borrow records
- Each item has **Return button**
- Calls:

  ```bash
  POST /borrows/{id}/return
  ```

---

# HTTP Status

| Status | Meaning                            |
| ------ | ---------------------------------- |
| 400    | Validation / business rule failure |
| 404    | Resource not found                 |
| 409    | Conflict (e.g. already borrowed)   |
| 201    | Created                            |
| 200    | Ok                                 |

---

# Example cURL Commands

## Create Book

```bash
curl -X POST http://localhost:8080/v1/library/books \
-H "Content-Type: application/json" \
-d '{"isbn":"9780134685991","title":"Effective Java","author":"Joshua Bloch"}'
```

---

## Borrow Book

```bash
curl -X POST http://localhost:8080/v1/library/borrows \
-H "Content-Type: application/json" \
-d '{"borrowerId":"<uuid>","bookId":"<uuid>","borrowMsg":"Reading"}'
```

---

## Return Book

```bash
curl -X POST http://localhost:8080/v1/library/borrows/{id}/return
```

---

# Data Model Summary

## Book

- `id (UUID)`
- `isbn`
- `title`
- `author`

## Borrower

- `id (UUID)`
- `name`
- `email`

## BorrowRecord

- `recordId`
- `book`
- `borrower`
- `borrowedAt`
- `returnedAt`

---

# Business Rules

- Same ISBN → multiple copies allowed
- Same ISBN → different title/author NOT allowed
- One book copy → one active borrower
- Active Books borrowed are not available i.e. returnedAt == null

---

# PostgreSQL for production

Ensure:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/library_db
    username: postgres
    password: postgres
```

---

# Run with Docker for production

```bash
docker compose up -d # or docker compose up --build
```

---

# Notes

- Uses **Spring Boot + JPA + PostgreSQL**
- UUID-based identifiers
- Clean layered architecture (Controller → Service → Repository)
- Validation via `@Valid`

---

# Access

- http://localhost:8080/api/v3/api-docs -- API docs
- http://localhost:8080/api/library.html -- Test HTML/js UI
- http://localhost:8080/api/swagger-ui/index.html -- Swagger

---

# Future Enhancements

- JWT Authentication (RBAC)
- Pagination and Mapper support
- Metrics & monitoring
- Caching (Redis)
- Event-driven borrowing (Kafka)
