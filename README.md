# Helpdeskchat

Helpdeskchat is a small Spring-based helpdesk/chat backend that integrates AI chat capabilities and ticket management with email notifications. It is built with Spring (Boot), Spring Data JPA and includes async email sending and AI chat controller with chat history.

This README describes how to build, run and configure the project, plus recommended production hardening steps.

---

## Features

- Ticket create / update / delete / lookup logic (service layer)
- Asynchronous email sending for ticket events (email attempts are logged)
- AI chat endpoints for synchronous and streaming responses
- Uses Spring Data JPA for persistence (MySQL in examples)
- Dockerfile and GitHub Actions CI/CD workflow included 

## Prerequisites

- Java 17+ (matching your build/runtime Java version)
- Maven 3.6+
- MySQL (or another supported JDBC database)
- (Optional) Docker for container images

## Project layout (important paths)

- `src/main/java/com/helpdeskchatbot/helpdeskchat` — application sources
- `src/main/resources/application.yml` — configuration (contains examples; move secrets to env vars in prod)
- `src/main/java/.../config` — application configuration (AI, async, CORS)
- `src/main/java/.../controller` — REST controllers (AI endpoints present)
- `src/main/java/.../service` — business logic (ticket service, event listeners)
- `src/main/java/.../tools` — helper tools (EmailTools for sending emails)
- `Dockerfile` — image build
- `.github/workflows/ci-cd.yml` — example CI/CD workflow

## Important configuration / environment variables

The project currently reads configuration from `application.yml`. For security and portability you should provide sensitive values via environment variables or a secrets manager. The following environment variables are referenced or recommended:

- SPRING_DATASOURCE_URL — JDBC url (example: `jdbc:mysql://localhost:3306/helpDeskChat?useSSL=false&serverTimezone=UTC`)
- SPRING_DATASOURCE_USERNAME — DB username
- SPRING_DATASOURCE_PASSWORD — DB password
- SPRING_MAIL_HOST — SMTP host (e.g. `smtp.gmail.com`)
- SPRING_MAIL_PORT — SMTP port (e.g. `587`)
- SPRING_MAIL_USERNAME — SMTP username
- SPRING_MAIL_PASSWORD — SMTP password (use app password/access token)
- APP_EMAIL_FROM — default From address for outgoing emails (e.g. `support@example.com`)
- OPENAI_API_KEY — API key for AI provider (if used)
- OPENAI_BASE_URL — optional base URL for AI provider
- OPENAI_MODEL — model id
- OPENAI_MAX_TOKENS — max tokens for AI responses

Example (PowerShell) to set env vars locally before run:

```powershell
$env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/helpDeskChat?useSSL=false&serverTimezone=UTC'
$env:SPRING_DATASOURCE_USERNAME = 'root'
$env:SPRING_DATASOURCE_PASSWORD = 'your_db_password'
$env:SPRING_MAIL_USERNAME = 'smtp-user@example.com'
$env:SPRING_MAIL_PASSWORD = 'smtp-password'
$env:APP_EMAIL_FROM = 'support@example.com'
```

On Linux/macOS (bash):

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/helpDeskChat?useSSL=false&serverTimezone=UTC'
export SPRING_DATASOURCE_USERNAME='root'
export SPRING_DATASOURCE_PASSWORD='your_db_password'
export SPRING_MAIL_USERNAME='smtp-user@example.com'
export SPRING_MAIL_PASSWORD='smtp-password'
export APP_EMAIL_FROM='support@example.com'
```

IMPORTANT: Never commit real secrets (DB passwords, SMTP credentials, API keys) into source control. Use CI/CD secrets or a secret manager in production.

## Build and run

Build (from project root):

```bash
mvn -B clean package
```

Run with Maven:

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/helpdeskchat-0.0.1-SNAPSHOT.jar
```

## Docker

Build locally:

```bash
docker build -t helpdeskchat:local .
```

Run container (example mapping port 8080):

```bash
docker run -e SPRING_DATASOURCE_URL='$SPRING_DATASOURCE_URL' \
  -e SPRING_DATASOURCE_USERNAME='$SPRING_DATASOURCE_USERNAME' \
  -e SPRING_DATASOURCE_PASSWORD='$SPRING_DATASOURCE_PASSWORD' \
  -e SPRING_MAIL_USERNAME='$SPRING_MAIL_USERNAME' \
  -e SPRING_MAIL_PASSWORD='$SPRING_MAIL_PASSWORD' \
  -p 8080:8080 helpdeskchat:local
```

## Endpoints (existing)

- AI endpoints (controller `AiController`):
  - POST `/api/v1/ai/query` — synchronous chat query (expects request body string and `ConversationId` header)
  - POST `/api/v1/ai/stream` — streaming chat responses (returns a Flux of strings; expects `conversationId` header)

Note: Ticket HTTP endpoints are implemented in the service layer (`TicketService`) but a dedicated `TicketController` may be present or added. The `TicketService` exposes methods for create/update/delete and lookups by username, email and ticket id.

When a ticket is created the application will:

- Persist the ticket to the database
- Publish a ticket-created event
- Send a confirmation email asynchronously to the user's email address and write an `EmailLog` record with the send status (PENDING, SENT, FAILED)

## Testing

Run unit tests:

```bash
mvn test
```

For integration tests that require a real DB, use Testcontainers in CI or point your application to a local/test MySQL instance.

## CI/CD

A GitHub Actions workflow exists in `.github/workflows/ci-cd.yml` which builds, tests and (optionally) builds and pushes a Docker image. Configure repository secrets (DockerHub/GCR credentials, SSH/Deploy keys) in GitHub to enable image publishing or remote deploy.

## Production hardening recommendations

1. Secrets management: remove any hard-coded credentials in `application.yml`. Use environment variables, Vault/Azure Key Vault/AWS Secrets Manager, or CI secrets.
2. DB migrations: stop using `spring.jpa.hibernate.ddl-auto=update` in production; use Flyway or Liquibase with versioned migrations.
3. Retry and observability: add retries for email sending (Spring Retry), expose metrics (Micrometer), and add health checks (Spring Actuator).
4. Email provider: use a production-class email provider (AWS SES, SendGrid, Mailgun) and use API/auth tokens rather than personal SMTP credentials.
5. Security: add authentication and authorization (Spring Security) for your REST endpoints. Add input validation and DTOs for controller payloads.
6. Logging/tracing: add correlation IDs, structured logging (JSON) and distributed tracing (OpenTelemetry) for production debugging.
7. Backups and monitoring: configure DB backups, monitor queue sizes (if you add a queue), and alert on email failures.

## Troubleshooting

- If emails are not sent:
  - Verify SMTP credentials and provider restrictions (Gmail requires app passwords / OAuth for programmatic sending).
  - Check application logs for SMTP exceptions.
  - Inspect `email_logs` table (if present) for stored error messages.

- If DB schema issues occur after changing entities:
  - Use Flyway migrations to apply controlled schema changes.
  - Do not rely on `ddl-auto: update` in production.

## Contributing

Contributions are welcome. Open issues or PRs with focused changes. Please avoid committing secrets.

## License

This repository does not include a license file. Add a LICENSE file if you plan to publish or open-source this project.

---

If you want, I can now:

- Convert `application.yml` to environment-variable-driven configuration and remove hard-coded secrets.
- Add Flyway migrations to manage schema changes (including removal of `unique` constraints if needed).
- Create DTOs and a `TicketController` to expose the ticket endpoints safely.

Pick one or more options and I will apply the changes and run the build/tests.

