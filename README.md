# URL Shortener

A URL shortening service built with Spring Boot and PostgreSQL. Users can register, log in, shorten URLs, track click analytics, and manage their links — all secured with JWT authentication.

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.3** — Web, Security, Data JPA, Validation
- **PostgreSQL** — Primary database
- **Hibernate** — ORM
- **jjwt 0.13.0** — JWT generation and validation
- **BCrypt** — Password hashing

## Features

- User registration and login with JWT-based authentication
- URL shortening using Base62 encoding on database IDs
- Redirect from short code to original URL with click tracking
- Per-user URL management (list, delete/deactivate)
- Click analytics per shortened URL
- Soft deletes — deactivated URLs preserve click history
- Global exception handling with consistent error responses
- Input validation on all endpoints


**Request flow:**
1. All protected requests pass through `JwtAuthFilter`, which validates the Bearer token and sets the security context.
2. Controllers extract the authenticated user's email from `SecurityContextHolder` and delegate to services.
3. Services interact with repositories and return DTOs — entities never leave the service layer.

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL running locally

### Database Setup

```sql
CREATE DATABASE urlshortener;
```

### Environment Variables

The application requires the following environment variables.

```
JWT_SECRET=<base64-encoded secret, at least 256 bits>
```

Set these in your IDE run configuration or shell before running.

### Running Locally

```bash
git clone https://github.com/OlivMaher/UrlShortener.git
cd UrlShortener
mvn spring-boot:run
```

The server starts on `http://localhost:8080`.

## API Endpoints

### Auth

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Log in and receive a JWT |

**Register request body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Login / Register response:**
```json
{
  "token": "<jwt>"
}
```

---

### URLs

All URL endpoints require `Authorization: Bearer <token>` header.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/urls/shorten` | Shorten a URL |
| GET | `/api/urls` | List all your URLs |
| DELETE | `/api/urls/{id}` | Deactivate a URL |
| GET | `/api/urls/{id}/analytics` | Get click count for a URL |

**Shorten request body:**
```json
{
  "originalUrl": "https://example.com/somepath"
}
```

**Shorten response:**
```json
{
  "shortCode": "b2Xk9"
}
```

---

### Redirect

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/{shortCode}` | Public | Redirects to the original URL (302) |

## Project Structure

```
src/
├── main/java/com/olivmaher/urlshortener/
│   ├── controller/       # AuthController, UrlController, RedirectController
│   ├── service/          # AuthService, UrlService
│   ├── repository/       # UserRepository, UrlRepository, ClickRepository
│   ├── entity/           # User, Url, Click
│   ├── dto/              # Request/Response DTOs
│   ├── security/         # JwtAuthFilter, SecurityConfig
│   ├── util/             # JwtUtil, Base62Encoder
│   └── exception/        # Custom exceptions, GlobalExceptionHandler
└── test/java/com/olivmaher/urlshortener/ # Base62EncoderTest, AuthServiceTest, UrlServiceTest
```


## Author

[OlivMaher](https://github.com/OlivMaher)
