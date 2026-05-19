# Candidate Onboarding Spring Boot MVP

A Java 17 Spring Boot application implementing a local-testable hiring and onboarding workflow derived from context `ctx_a9758191c0d5`.

## Features
- Project and staffing request management
- Candidate sourcing with one-resume-per-candidate validation
- Skill-based candidate matching with mandatory-skill gating
- Interview scheduling and completion
- Shortlist decision enforcement after completed interview feedback
- Face-to-face meeting workflow
- Onboarding initiation only after successful meeting completion
- HR notifications for external candidates
- Local OpenAPI and Actuator endpoints

## Tech
- Java 17
- Spring Boot 3.3.x
- Maven
- Spring Web, Validation, Actuator
- Spring Data JPA
- H2 in-memory database
- springdoc OpenAPI

## Run locally
```bash
mvn spring-boot:run
```

App URLs:
- API base: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- Health: `http://localhost:8080/actuator/health`
- H2 Console: `http://localhost:8080/h2-console`

## Test
```bash
mvn test
```

Single test:
```bash
mvn -Dtest=HiringWorkflowControllerTest test
```

## Example API flow

Create a project:
```bash
curl -X POST http://localhost:8080/api/projects ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Apollo\",\"technologyStack\":[\"Java\",\"Spring Boot\"],\"status\":\"PLANNING\",\"startDate\":\"2026-05-20\",\"commitments\":\"Build hiring platform\",\"managerName\":\"Anita Manager\"}"
```

Create a staffing request:
```bash
curl -X POST http://localhost:8080/api/staffing-requests ^
  -H "Content-Type: application/json" ^
  -d "{\"projectId\":\"<PROJECT_ID>\",\"numberOfPositions\":2,\"urgency\":\"HIGH\",\"skills\":[{\"name\":\"Java\",\"proficiency\":\"ADVANCED\",\"minimumYearsOfExperience\":3,\"mandatory\":true},{\"name\":\"Spring Boot\",\"proficiency\":\"INTERMEDIATE\",\"minimumYearsOfExperience\":2,\"mandatory\":false}]}"
```

Create an external candidate:
```bash
curl -X POST http://localhost:8080/api/candidates ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Riya Sen\",\"email\":\"riya@example.com\",\"phone\":\"9999999999\",\"source\":\"EXTERNAL\",\"skills\":[{\"name\":\"Java\",\"proficiency\":\"EXPERT\",\"minimumYearsOfExperience\":5,\"mandatory\":true},{\"name\":\"Spring Boot\",\"proficiency\":\"ADVANCED\",\"minimumYearsOfExperience\":4,\"mandatory\":false}],\"resume\":{\"sourceName\":\"LinkedIn\",\"sourceType\":\"EXTERNAL\",\"url\":\"https://example.com/resume/riya\",\"lastUpdated\":\"2026-05-19T10:00:00Z\",\"summary\":\"Senior backend engineer\",\"totalExperienceYears\":6}}"
```

Get matches:
```bash
curl http://localhost:8080/api/staffing-requests/<REQUEST_ID>/matches
```

Schedule interview:
```bash
curl -X POST http://localhost:8080/api/interviews ^
  -H "Content-Type: application/json" ^
  -d "{\"candidateId\":\"<CANDIDATE_ID>\",\"staffingRequestId\":\"<REQUEST_ID>\",\"scheduledAt\":\"2026-06-01T10:00:00Z\",\"interviewType\":\"TECHNICAL\",\"panelMembers\":[\"Lead Engineer\"],\"durationMinutes\":60}"
```

Complete interview:
```bash
curl -X PATCH http://localhost:8080/api/interviews/<INTERVIEW_ID>/complete ^
  -H "Content-Type: application/json" ^
  -d "{\"feedback\":\"Strong system design and Java knowledge\",\"overallScore\":88}"
```

Create shortlist decision:
```bash
curl -X POST http://localhost:8080/api/shortlist-decisions ^
  -H "Content-Type: application/json" ^
  -d "{\"interviewId\":\"<INTERVIEW_ID>\",\"candidateId\":\"<CANDIDATE_ID>\",\"status\":\"selected\",\"reasoning\":\"Excellent match\",\"overallScore\":88}"
```

Schedule face-to-face meeting:
```bash
curl -X POST http://localhost:8080/api/meetings ^
  -H "Content-Type: application/json" ^
  -d "{\"shortListDecisionId\":\"<DECISION_ID>\",\"candidateId\":\"<CANDIDATE_ID>\",\"projectId\":\"<PROJECT_ID>\",\"location\":\"Bangalore Office\",\"scheduledAt\":\"2026-06-03T11:00:00Z\",\"agenda\":\"Project overview and commitments\"}"
```

Complete meeting:
```bash
curl -X PATCH http://localhost:8080/api/meetings/<MEETING_ID>/complete ^
  -H "Content-Type: application/json" ^
  -d "{\"agreementReached\":true,\"outcome\":\"Candidate accepted terms\"}"
```

Initiate onboarding:
```bash
curl -X POST http://localhost:8080/api/onboardings ^
  -H "Content-Type: application/json" ^
  -d "{\"candidateId\":\"<CANDIDATE_ID>\",\"projectId\":\"<PROJECT_ID>\",\"faceToFaceMeetingId\":\"<MEETING_ID>\",\"trainingProgramName\":\"Platform Onboarding\",\"expectedCompletionDate\":\"2026-06-20\"}"
```

## Notes
- APIs persist and query workflow data through an H2 in-memory database via Spring Data JPA.
- The H2 datasource is configured in [`src/main/resources/application.yml`](src/main/resources/application.yml) and is recreated on application startup.
- Outlook, Teams, and LinkedIn integrations are stubbed through generated links/config placeholders.
- Business rules from the requirements are enforced in service logic, not only at the API boundary.