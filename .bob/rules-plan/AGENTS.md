# AGENTS.md

This file provides guidance to agents when working with code in this repository.

- No implementation is present locally, so planning should start from the requirement ontology in `ctx_a9758191c0d5`, not from assumed framework structure.
- Keep designs schema-first around `Project`, `StaffingRequest`, `Candidate`, `SkillRequirement`, `Interview`, `ShortlistDecision`, `FaceToFaceMeeting`, `OnboardingWorkflow`, `Resume`, `ResumeSource`, `TrainingProgram`, and `Stakeholder`.
- Preserve requirement states and enums exactly in plans; do not normalize them into alternative status vocabularies.
- Architectural flow has hard gates that must appear in designs: project activation requires tech stack + manager; shortlist requires completed interview feedback; onboarding requires completed face-to-face meeting and agreement.
- Candidate matching is not generic scoring: mandatory skills are gating, internal candidates have priority, and score `> 70` triggers automatic consideration.
- Resume and sourcing architecture are constrained: one resume per candidate, every resume linked to a source, and sourcing spans internal employee DB plus external platforms.
- Interview scheduling architecture must account for Outlook calendar checks, Teams link generation, and panel composition aligned to technology requirements.
- Notifications and generated artifacts are required subsystems: HR notifications for all external candidates, plus auto-regenerated architecture docs and code walkthroughs on codebase change.