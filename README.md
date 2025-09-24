# Exagrad Student Service

ExaGrad Student Service is a Spring Boot–based application designed to manage
student-related
operations at SAU
in connection with exams and grades. It leverages modern Java (Java 21) and
integrates with various
Spring Boot
modules for web, data, and testing functionalities.

## Local MinIO development

```bash
docker compose up -d
```

- API: http://localhost:9000
- Web UI: http://localhost:9001

## Prerequisites

- Java 21 or higher
- Maven 3.8 or higher

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/Agile-Software-Engineering-25/team-13-backend-exagrad-student-service.git
cd team-13-backend-exagrad-student-service
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080

## Testing

### Unit Tests

Run unit tests using Maven:

```bash
mvn test
```

## Code Style & Linting

This project uses Checkstyle (CLI jar) and EditorConfig to enforce a consistent
Java code style.

```
java -jar checkstyle-11.0.0-all.jar -c checkstyle.xml -f plain src/main/java src/test/java
```

- IDE auto-formatting:
    - `.editorconfig` sets 2‑space indentation for `*.java` and YAML, trims
      trailing whitespace, and
      enforces final newline.
    - IntelliJ import layout is aligned with our import groups.

Adjust rules in `checkstyle.xml`; IDE basics are in `.editorconfig`.

## Dependencies

The project uses the following key dependencies:

- `spring-boot-starter-web`: For building web applications, including RESTful
  services.
- `spring-boot-starter-data-jpa`: For integrating with JPA and databases.
- `spring-boot-starter-test`: For testing support, including JUnit 5.
- `h2`: An in-memory database for development and testing purposes.
- `lombok`: For reducing boilerplate code in Java classes.
- `spring-boot-starter-validation`: For validating user input.
- `spring-boot-starter-security`: For securing the application with basic
  authentication.
- `springdoc-openapi-ui`: For generating OpenAPI documentation and Swagger UI.
- `spring-boot-starter-actuator`: For monitoring and managing the application.
