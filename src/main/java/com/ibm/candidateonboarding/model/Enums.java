package com.ibm.candidateonboarding.model;

public final class Enums {

    private Enums() {
    }

    public enum ProjectStatus {
        PLANNING,
        ACTIVE,
        ON_HOLD,
        COMPLETED
    }

    public enum StaffingRequestStatus {
        RequestOpen,
        RequestInProgress,
        RequestFulfilled,
        RequestCancelled
    }

    public enum CandidateStatus {
        CandidateSourced,
        CandidateScreening,
        CandidateInterviewing,
        CandidateShortlisted,
        CandidateRejected,
        CandidateHired
    }

    public enum SkillProficiency {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }

    public enum UrgencyLevel {
        HIGH,
        MEDIUM,
        LOW
    }

    public enum CandidateSource {
        INTERNAL,
        EXTERNAL
    }

    public enum InterviewStatus {
        InterviewScheduled,
        InterviewCompleted,
        InterviewCancelled
    }

    public enum InterviewType {
        TECHNICAL,
        BEHAVIORAL,
        MANAGERIAL,
        HR
    }

    public enum MeetingStatus {
        MeetingScheduled,
        MeetingCompleted,
        MeetingCancelled
    }

    public enum OnboardingStatus {
        OnboardingInitiated,
        OnboardingInProgress,
        OnboardingCompleted
    }
}

// Made with Bob
