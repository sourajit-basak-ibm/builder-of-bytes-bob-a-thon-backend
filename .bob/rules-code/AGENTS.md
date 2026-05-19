# AGENTS.md

This file provides guidance to agents when working with code in this repository.

- No runnable source exists yet; avoid inventing framework-specific patterns, scripts, or file locations.
- Treat the requirements context as schema-first: implementations should center on `Project`, `StaffingRequest`, `Candidate`, `SkillRequirement`, `Interview`, `ShortlistDecision`, `FaceToFaceMeeting`, `OnboardingWorkflow`, `Resume`, `ResumeSource`, `TrainingProgram`, and `Stakeholder`.
- Preserve requirement-defined enum values exactly; do not rename status values or “clean them up”.
- Enforce business gates in code paths, not just UI validation: project activation needs tech stack + manager, shortlist decisions need completed interview feedback, onboarding needs completed face-to-face meeting.
- Matching is constrained: mandatory skills must match, internal candidates take priority, and match score `> 70` triggers automatic consideration.
- Resume handling is not many-to-many: one candidate has exactly one resume, and every resume must reference a source plus URL/last-update metadata.
- HR notification behavior is mandatory for all external candidates.
- Generated architecture/code walkthrough artifacts are part of the product model and should be refreshed on codebase changes.
- In Code mode you do not have MCP or Browser access, so do not rely on external context lookups during implementation.