# Book Store Application

A modern Spring Boot application for managing a book inventory with a clean user interface and comprehensive testing.

## Features

- **Book Management**: Create, read, update, and delete books
- **REST API**: Full CRUD operations via RESTful endpoints
- **Web Interface**: User-friendly UI with modern yellow and black theme
- **Data Persistence**: H2 in-memory database with JPA
- **Validation**: Input validation for all book properties

## Technology Stack

- **Backend**: Java 18, Spring Boot 3.2.3
- **Frontend**: Thymeleaf, Bootstrap, Font Awesome
- **Database**: H2 (in-memory)
- **Build Tool**: Gradle
- **Testing**: JUnit 5, Spring Test, Bruno API Tests

## CI/CD Pipeline

This project includes a comprehensive CI/CD setup with GitHub Actions:

- **Build & Test**: Automated build and unit testing
- **Code Quality**: 
  - Checkstyle for code style enforcement
  - SonarQube for code quality analysis
  - CodeQL for security scanning
- **API Testing**: Bruno API tests for endpoint verification
- **Dependency Management**: Dependabot for automatic dependency updates

## Getting Started

### Prerequisites

- Java 18 or higher
- Gradle (or use the included Gradle wrapper)

### Running the Application

```bash
# Clone the repository
git clone https://github.com/your-username/goit-demo-app.git
cd goit-demo-app

# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

The application will be available at http://localhost:8080

### Running Tests

```bash
# Run all tests
./gradlew test

# Run only API tests (requires Bruno CLI)
npm install -g @usebruno/cli
bru run --env local ./bruno/collections/book-api
```

## API Endpoints

| Method | URL                  | Description           |
|--------|----------------------|-----------------------|
| GET    | /api/books           | Get all books         |
| GET    | /api/books/{id}      | Get book by ID        |
| POST   | /api/books           | Create a new book     |
| PUT    | /api/books/{id}      | Update an existing book |
| DELETE | /api/books/{id}      | Delete a book         |
| GET    | /api/books/search    | Search books by title |

## Web Interface

| URL                  | Description                |
|----------------------|----------------------------|
| /                    | Home page                  |
| /books               | View all books             |
| /books/{id}          | View book details          |
| /books/new           | Create a new book          |
| /books/{id}/edit     | Edit an existing book      |

## Project Structure

```
goit-demo-app/
├── .github/                   # GitHub Actions workflows and Dependabot config
├── bruno/                     # Bruno API tests
├── config/                    # Configuration files (Checkstyle, etc.)
├── src/
│   ├── main/
│   │   ├── java/              # Java source code
│   │   └── resources/         # Application resources and templates
│   └── test/                  # Test code
└── build.gradle               # Gradle build configuration
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot team for the excellent framework
- H2 Database for the lightweight database solution
- Bruno for the API testing capabilities
