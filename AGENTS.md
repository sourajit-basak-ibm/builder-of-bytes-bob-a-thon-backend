# AGENTS.md

This file provides guidance to agents when working with code in this repository.

- The repository now contains a Maven-based Spring Boot Java 17 app; use [`pom.xml`](pom.xml) as the source of truth for dependencies and build lifecycle.
- Verified local commands:
  - Run app: `mvn spring-boot:run`
  - Run all tests: `mvn test`
  - Run a single test: `mvn -Dtest=HiringWorkflowControllerTest test`
- The current implementation uses Spring Data JPA with an H2 in-memory database for local API testability; use [`src/main/resources/application.yml`](src/main/resources/application.yml) as the source of truth for datasource and JPA settings.
- Core workflow logic is centralized in [`HiringWorkflowService`](src/main/java/com/ibm/candidateonboarding/service/HiringWorkflowService.java:41), not in controllers.
- Business gates from requirements are enforced in service code: project activation requires tech stack + manager, shortlist decisions require completed interview feedback, onboarding requires completed face-to-face meeting with agreement.
- Preserve requirement-defined enums exactly from [`Enums`](src/main/java/com/ibm/candidateonboarding/model/Enums.java:3); do not rename statuses to “cleaner” alternatives.
- Matching is constrained: mandatory skills must match, internal candidates get priority, and score `> 70` drives auto-consideration.
- Resume modeling is intentionally strict: one candidate has exactly one resume, and each resume must include source and URL metadata.
- External-candidate HR notification is part of the implemented workflow and is surfaced via the notifications API.
- Local API surface is exposed from [`HiringWorkflowController`](src/main/java/com/ibm/candidateonboarding/api/HiringWorkflowController.java:29); use [`README.md`](README.md) and Swagger UI for request examples.
- OpenAPI and local observability are enabled at `/swagger-ui.html`, `/api-docs`, and `/actuator/health`.
- Current tests are end-to-end MockMvc tests in [`HiringWorkflowControllerTest`](src/test/java/com/ibm/candidateonboarding/HiringWorkflowControllerTest.java:18); extend those flows when adding features rather than creating disconnected unit-only coverage.