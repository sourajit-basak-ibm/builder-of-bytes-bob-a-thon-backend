package com.ibm.candidateonboarding.api;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import com.ibm.candidateonboarding.model.Enums.CandidateSource;
import com.ibm.candidateonboarding.model.Enums.CandidateStatus;
import com.ibm.candidateonboarding.model.Enums.InterviewStatus;
import com.ibm.candidateonboarding.model.Enums.InterviewType;
import com.ibm.candidateonboarding.model.Enums.MeetingStatus;
import com.ibm.candidateonboarding.model.Enums.OnboardingStatus;
import com.ibm.candidateonboarding.model.Enums.ProjectStatus;
import com.ibm.candidateonboarding.model.Enums.SkillProficiency;
import com.ibm.candidateonboarding.model.Enums.StaffingRequestStatus;
import com.ibm.candidateonboarding.model.Enums.UrgencyLevel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class ApiModels {

    private ApiModels() {
    }

    public record ProjectRequest(
            @NotBlank String name,
            @NotEmpty List<@NotBlank String> technologyStack,
            @NotNull ProjectStatus status,
            @NotNull LocalDate startDate,
            @NotBlank String commitments,
            String managerName
    ) {
    }

    public record ProjectResponse(
            String id,
            String name,
            List<String> technologyStack,
            ProjectStatus status,
            LocalDate startDate,
            String commitments,
            String managerName
    ) {
    }

    public record SkillRequest(
            @NotBlank String name,
            @NotNull SkillProficiency proficiency,
            @Min(0) Integer minimumYearsOfExperience,
            boolean mandatory
    ) {
    }

    public record SkillResponse(
            String id,
            String name,
            SkillProficiency proficiency,
            Integer minimumYearsOfExperience,
            boolean mandatory
    ) {
    }

    public record StaffingRequestCreateRequest(
            @NotBlank String projectId,
            @Positive int numberOfPositions,
            @NotNull UrgencyLevel urgency,
            @NotEmpty List<@Valid SkillRequest> skills
    ) {
    }

    public record StaffingRequestResponse(
            String id,
            String projectId,
            int numberOfPositions,
            UrgencyLevel urgency,
            StaffingRequestStatus status,
            List<SkillResponse> skills
    ) {
    }

    public record CandidateCreateRequest(
            @NotBlank String name,
            @Email @NotBlank String email,
            @NotBlank String phone,
            @NotNull CandidateSource source,
            @NotEmpty List<@Valid SkillRequest> skills,
            @Valid @NotNull ResumeRequest resume
    ) {
    }

    public record ResumeRequest(
            @NotBlank String sourceName,
            @NotBlank String sourceType,
            @NotBlank String url,
            @NotNull OffsetDateTime lastUpdated,
            @NotBlank String summary,
            @Min(0) int totalExperienceYears
    ) {
    }

    public record ResumeResponse(
            String sourceName,
            String sourceType,
            String url,
            OffsetDateTime lastUpdated,
            String summary,
            int totalExperienceYears
    ) {
    }

    public record CandidateResponse(
            String id,
            String name,
            String email,
            String phone,
            CandidateSource source,
            CandidateStatus status,
            List<SkillResponse> skills,
            ResumeResponse resume
    ) {
    }

    public record MatchResponse(
            String candidateId,
            String candidateName,
            int score,
            boolean mandatorySkillsMatched,
            boolean autoConsidered,
            boolean internalPriority
    ) {
    }

    public record InterviewScheduleRequest(
            @NotBlank String candidateId,
            @NotBlank String staffingRequestId,
            @Future @NotNull OffsetDateTime scheduledAt,
            @NotNull InterviewType interviewType,
            @NotEmpty List<@NotBlank String> panelMembers,
            @Positive int durationMinutes
    ) {
    }

    public record InterviewCompleteRequest(
            @NotBlank String feedback,
            @Min(0) @Max(100) Integer overallScore
    ) {
    }

    public record InterviewResponse(
            String id,
            String candidateId,
            String staffingRequestId,
            OffsetDateTime scheduledAt,
            InterviewType interviewType,
            InterviewStatus status,
            List<String> panelMembers,
            int durationMinutes,
            String teamsLink,
            String feedback,
            Integer overallScore
    ) {
    }

    public record ShortlistDecisionRequest(
            @NotBlank String interviewId,
            @NotBlank String candidateId,
            @NotBlank String status,
            @NotBlank String reasoning,
            @Min(0) @Max(100) Integer overallScore
    ) {
    }

    public record ShortlistDecisionResponse(
            String id,
            String interviewId,
            String candidateId,
            String status,
            String reasoning,
            Integer overallScore,
            boolean notificationTriggered
    ) {
    }

    public record FaceToFaceMeetingRequest(
            @NotBlank String shortListDecisionId,
            @NotBlank String candidateId,
            @NotBlank String projectId,
            @NotBlank String location,
            @Future @NotNull OffsetDateTime scheduledAt,
            @NotBlank String agenda
    ) {
    }

    public record FaceToFaceMeetingCompleteRequest(
            @NotNull Boolean agreementReached,
            @NotBlank String outcome
    ) {
    }

    public record FaceToFaceMeetingResponse(
            String id,
            String shortListDecisionId,
            String candidateId,
            String projectId,
            String location,
            OffsetDateTime scheduledAt,
            String agenda,
            MeetingStatus status,
            Boolean agreementReached,
            String outcome
    ) {
    }

    public record OnboardingInitiateRequest(
            @NotBlank String candidateId,
            @NotBlank String projectId,
            @NotBlank String faceToFaceMeetingId,
            @NotBlank String trainingProgramName,
            @NotNull LocalDate expectedCompletionDate
    ) {
    }

    public record OnboardingProgressRequest(
            @Min(0) @Max(100) int progress,
            @NotNull OnboardingStatus status
    ) {
    }

    public record OnboardingWorkflowResponse(
            String id,
            String candidateId,
            String projectId,
            String faceToFaceMeetingId,
            String trainingProgramName,
            LocalDate expectedCompletionDate,
            int progress,
            OnboardingStatus status,
            boolean hrNotificationSent
    ) {
    }

    public record NotificationResponse(
            String id,
            String recipient,
            String message,
            OffsetDateTime createdAt
    ) {
    }

    public record HealthResponse(
            String status,
            String application
    ) {
    }
}
