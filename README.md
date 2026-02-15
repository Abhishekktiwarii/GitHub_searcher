# 📦 GitHub Repository Searcher API

A Spring Boot REST API that allows users to search GitHub repositories using the GitHub REST API, store the results in a database, and retrieve stored repositories using filtering and sorting criteria.

This project demonstrates clean backend architecture, external API integration, database persistence, dynamic filtering, and proper error handling.

---

# 🚀 Features

✅ Fetch repositories from GitHub Search API
✅ Store repositories in H2 database
✅ Update existing repositories (UPSERT logic)
✅ Retrieve stored repositories with filters
✅ Dynamic filtering using JPA Specifications
✅ Sorting by stars, forks, or last updated date
✅ Global exception handling
✅ Input validation
✅ REST-compliant APIs
✅ Testable using Postman (No UI)

---

# 🧠 Architecture

The application follows a layered architecture:

```
Controller → Service → Repository → Database
                   ↓
              GitHub API
```

## Layers

* **Controller** → Exposes REST endpoints
* **Service** → Business logic and API integration
* **Repository** → Database operations (JPA)
* **Specification** → Dynamic database filtering
* **DTO** → Request and response models
* **Exception Handler** → Global error handling

---

# 🛠 Tech Stack

* Java 17+
* Spring Boot
* Spring Web (REST APIs)
* Spring Data JPA
* H2 Database (In-Memory)
* WebClient (GitHub API integration)
* Lombok
* Maven

---

# 📂 Project Structure

```
src/main/java/com/code/github_searcher
│
├── controller
│   └── GithubController
│
├── service
│   ├── GithubService
│   └── impl/GithubServiceImpl
│
├── repository
│   ├── GithubRepositoryJpaRepository
│   └── GithubRepositorySpecification
│
├── entity
│   └── GithubRepositoryEntity
│
├── dto
│   ├── GithubSearchRequest
│   └── RepositoryResponse
│
├── config
│   └── WebClientConfig
│
└── exception
    └── GlobalExceptionHandler
```

---

# ⚙️ How To Run The Project

## 1. Clone Repository

```
git clone <your-repo-url>
cd github-searcher
```

---

## 2. Build Project

```
mvn clean install
```

---

## 3. Run Application

```
mvn spring-boot:run
```

Application starts on:

```
http://localhost:8080
```

---

# 🗄 Database Access (H2 Console)

Since H2 is in-memory, no external database setup is required.

Open:

```
http://localhost:8080/h2-console
```

Use:

```
JDBC URL: jdbc:h2:mem:githubdb
Username: sa
Password: (leave empty)
```

You can query stored repositories:

```
SELECT * FROM repositories;
```

---

# 🔌 API Endpoints

---

## 1️⃣ Search GitHub Repositories

Fetch repositories from GitHub and store them in database.

### Endpoint

```
POST /api/github/search
```

### Request Body

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

### Parameters

| Field    | Description                            |
| -------- | -------------------------------------- |
| query    | Repository name or keyword             |
| language | Programming language filter (optional) |
| sort     | stars, forks, or updated               |

---

### Example Request (Postman)

```
POST http://localhost:8080/api/github/search
```

---

### Example Response

```json
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [
    {
      "id": 123456,
      "name": "spring-boot-example",
      "description": "Example repository",
      "owner": "user123",
      "language": "Java",
      "stars": 450,
      "forks": 120,
      "lastUpdated": "2024-01-01T12:00:00Z"
    }
  ]
}
```

---

## 2️⃣ Retrieve Stored Repositories

Fetch stored repositories from database using filters.

### Endpoint

```
GET /api/github/repositories
```

### Query Parameters (Optional)

| Parameter | Description                    |
| --------- | ------------------------------ |
| language  | Filter by programming language |
| minStars  | Minimum stars count            |
| sort      | stars, forks, updated          |

---

### Example Requests

```
GET http://localhost:8080/api/github/repositories
```

```
GET http://localhost:8080/api/github/repositories?language=Java
```

```
GET http://localhost:8080/api/github/repositories?minStars=50000
```

```
GET http://localhost:8080/api/github/repositories?language=Java&minStars=1000&sort=forks
```

---

# 🧪 How To Test (Step-by-Step)

## Step 1 — Start Application

Run the application using:

```
mvn spring-boot:run
```

---

## Step 2 — Fetch Data From GitHub

Send POST request:

```
POST /api/github/search
```

This:

* Calls GitHub API
* Stores results in database
* Returns stored data

---

## Step 3 — Verify Data Stored

Open H2 console and run:

```
SELECT * FROM repositories;
```

---

## Step 4 — Test Filtering

Try:

```
GET /api/github/repositories?minStars=50000
```

Only high-star repositories should appear.

---

## Step 5 — Test Sorting

Try:

```
GET /api/github/repositories?sort=forks
```

Results sorted by forks.

---

## Step 6 — Test UPSERT Behavior

Call the search endpoint multiple times.

Verify:

```
SELECT COUNT(*) FROM repositories;
```

Count should not duplicate existing repositories.

---

# 🧠 Design Decisions

### Why H2 Database?

* Easy setup
* No external dependency
* Ideal for testing and evaluation

### Why WebClient?

* Modern non-blocking HTTP client
* Better performance than RestTemplate

### Why JPA Specification?

* Dynamic filtering
* Clean query construction
* Scalable for large datasets

### Why Layered Architecture?

* Separation of concerns
* Maintainability
* Testability

---

# ⚠️ Error Handling

The application handles:

* Invalid input validation
* GitHub API errors
* Rate limit exceptions
* Empty results
* Runtime failures

Errors return structured JSON responses.

---

# 🔄 Expected Behavior

* Repository ID is unique
* Existing repository → updated (not duplicated)
* Filtering happens at database level
* Sorting handled at database level

---

# 👨‍💻 Author

Abhishek Tiwari

---

# ✅ Future Improvements

* Pagination support
* Caching GitHub results
* Replace H2 with PostgreSQL
* Authentication support
* Integration tests
* Swagger API documentation
