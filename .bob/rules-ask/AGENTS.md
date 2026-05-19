# AGENTS.md

This file provides guidance to agents when working with code in this repository.

- This repository currently exposes almost no local source; answers about implementation details must be framed as requirement-derived, not code-verified.
- The authoritative project context lives in `ctx_a9758191c0d5`; when local files are missing, prefer requirement constraints from that context over speculation.
- The product model is schema-first and revolves around `Project`, `StaffingRequest`, `Candidate`, `SkillRequirement`, `Interview`, `ShortlistDecision`, `FaceToFaceMeeting`, `OnboardingWorkflow`, `Resume`, `ResumeSource`, `TrainingProgram`, and `Stakeholder`.
- Several rules are easy to miss when answering questions: project activation requires tech stack + manager, shortlist decisions require completed interview feedback, onboarding requires completed face-to-face meeting.
- Matching is constrained by mandatory skills, internal-candidate priority, and auto-consideration for match score `> 70`.
- Resume handling is tightly constrained: exactly one resume per candidate, with source, URL, and last-update metadata.
- HR notification for external candidates is mandatory, not optional workflow detail.
- Generated architecture documentation and code walkthroughs are first-class outputs expected to refresh on codebase changes.