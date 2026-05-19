package com.ibm.candidateonboarding.persistence;

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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class WorkflowEntities {

    private WorkflowEntities() {
    }

    @Entity
    @Table(name = "projects")
    public static class ProjectEntity {
        @Id
        private String id;

        @Column(nullable = false, unique = true)
        private String name;

        @ElementCollection(fetch = FetchType.EAGER)
        @jakarta.persistence.CollectionTable(name = "project_technology_stack", joinColumns = @JoinColumn(name = "project_id"))
        @Column(name = "technology")
        @OrderColumn(name = "stack_index")
        private List<String> technologyStack = new ArrayList<>();

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ProjectStatus status;

        @Column(nullable = false)
        private LocalDate startDate;

        @Column(nullable = false)
        private String commitments;

        private String managerName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getTechnologyStack() {
            return technologyStack;
        }

        public void setTechnologyStack(List<String> technologyStack) {
            this.technologyStack = technologyStack;
        }

        public ProjectStatus getStatus() {
            return status;
        }

        public void setStatus(ProjectStatus status) {
            this.status = status;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public String getCommitments() {
            return commitments;
        }

        public void setCommitments(String commitments) {
            this.commitments = commitments;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }
    }

    @Entity
    @Table(name = "skills")
    public static class SkillEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private SkillProficiency proficiency;

        private Integer minimumYearsOfExperience;

        @Column(nullable = false)
        private boolean mandatory;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SkillProficiency getProficiency() {
            return proficiency;
        }

        public void setProficiency(SkillProficiency proficiency) {
            this.proficiency = proficiency;
        }

        public Integer getMinimumYearsOfExperience() {
            return minimumYearsOfExperience;
        }

        public void setMinimumYearsOfExperience(Integer minimumYearsOfExperience) {
            this.minimumYearsOfExperience = minimumYearsOfExperience;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }
    }

    @Entity
    @Table(name = "resumes")
    public static class ResumeEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String sourceName;

        @Column(nullable = false)
        private String sourceType;

        @Column(nullable = false)
        private String url;

        @Column(nullable = false)
        private OffsetDateTime lastUpdated;

        @Lob
        private String summary;

        @Column(nullable = false)
        private int totalExperienceYears;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public OffsetDateTime getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(OffsetDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public int getTotalExperienceYears() {
            return totalExperienceYears;
        }

        public void setTotalExperienceYears(int totalExperienceYears) {
            this.totalExperienceYears = totalExperienceYears;
        }
    }

    @Entity
    @Table(name = "candidates")
    public static class CandidateEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String phone;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private CandidateSource source;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private CandidateStatus status;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "candidate_id")
        private List<SkillEntity> skills = new ArrayList<>();

        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "resume_id")
        private ResumeEntity resume;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public CandidateSource getSource() {
            return source;
        }

        public void setSource(CandidateSource source) {
            this.source = source;
        }

        public CandidateStatus getStatus() {
            return status;
        }

        public void setStatus(CandidateStatus status) {
            this.status = status;
        }

        public List<SkillEntity> getSkills() {
            return skills;
        }

        public void setSkills(List<SkillEntity> skills) {
            this.skills = skills;
        }

        public ResumeEntity getResume() {
            return resume;
        }

        public void setResume(ResumeEntity resume) {
            this.resume = resume;
        }
    }

    @Entity
    @Table(name = "staffing_requests")
    public static class StaffingRequestEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String projectId;

        @Column(nullable = false)
        private int numberOfPositions;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private UrgencyLevel urgency;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StaffingRequestStatus status;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "staffing_request_id")
        private List<SkillEntity> skills = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public int getNumberOfPositions() {
            return numberOfPositions;
        }

        public void setNumberOfPositions(int numberOfPositions) {
            this.numberOfPositions = numberOfPositions;
        }

        public UrgencyLevel getUrgency() {
            return urgency;
        }

        public void setUrgency(UrgencyLevel urgency) {
            this.urgency = urgency;
        }

        public StaffingRequestStatus getStatus() {
            return status;
        }

        public void setStatus(StaffingRequestStatus status) {
            this.status = status;
        }

        public List<SkillEntity> getSkills() {
            return skills;
        }

        public void setSkills(List<SkillEntity> skills) {
            this.skills = skills;
        }
    }

    @Entity
    @Table(name = "interviews")
    public static class InterviewEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String candidateId;

        @Column(nullable = false)
        private String staffingRequestId;

        @Column(nullable = false)
        private OffsetDateTime scheduledAt;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private InterviewType interviewType;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private InterviewStatus status;

        @ElementCollection(fetch = FetchType.EAGER)
        @jakarta.persistence.CollectionTable(name = "interview_panel_members", joinColumns = @JoinColumn(name = "interview_id"))
        @Column(name = "panel_member")
        @OrderColumn(name = "member_order")
        private List<String> panelMembers = new ArrayList<>();

        @Column(nullable = false)
        private int durationMinutes;

        @Column(nullable = false)
        private String teamsLink;

        private String feedback;

        private Integer overallScore;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(String candidateId) {
            this.candidateId = candidateId;
        }

        public String getStaffingRequestId() {
            return staffingRequestId;
        }

        public void setStaffingRequestId(String staffingRequestId) {
            this.staffingRequestId = staffingRequestId;
        }

        public OffsetDateTime getScheduledAt() {
            return scheduledAt;
        }

        public void setScheduledAt(OffsetDateTime scheduledAt) {
            this.scheduledAt = scheduledAt;
        }

        public InterviewType getInterviewType() {
            return interviewType;
        }

        public void setInterviewType(InterviewType interviewType) {
            this.interviewType = interviewType;
        }

        public InterviewStatus getStatus() {
            return status;
        }

        public void setStatus(InterviewStatus status) {
            this.status = status;
        }

        public List<String> getPanelMembers() {
            return panelMembers;
        }

        public void setPanelMembers(List<String> panelMembers) {
            this.panelMembers = panelMembers;
        }

        public int getDurationMinutes() {
            return durationMinutes;
        }

        public void setDurationMinutes(int durationMinutes) {
            this.durationMinutes = durationMinutes;
        }

        public String getTeamsLink() {
            return teamsLink;
        }

        public void setTeamsLink(String teamsLink) {
            this.teamsLink = teamsLink;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public Integer getOverallScore() {
            return overallScore;
        }

        public void setOverallScore(Integer overallScore) {
            this.overallScore = overallScore;
        }
    }

    @Entity
    @Table(name = "shortlist_decisions")
    public static class ShortlistDecisionEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String interviewId;

        @Column(nullable = false)
        private String candidateId;

        @Column(nullable = false)
        private String status;

        @Column(nullable = false)
        private String reasoning;

        private Integer overallScore;

        @Column(nullable = false)
        private boolean notificationTriggered;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInterviewId() {
            return interviewId;
        }

        public void setInterviewId(String interviewId) {
            this.interviewId = interviewId;
        }

        public String getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(String candidateId) {
            this.candidateId = candidateId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReasoning() {
            return reasoning;
        }

        public void setReasoning(String reasoning) {
            this.reasoning = reasoning;
        }

        public Integer getOverallScore() {
            return overallScore;
        }

        public void setOverallScore(Integer overallScore) {
            this.overallScore = overallScore;
        }

        public boolean isNotificationTriggered() {
            return notificationTriggered;
        }

        public void setNotificationTriggered(boolean notificationTriggered) {
            this.notificationTriggered = notificationTriggered;
        }
    }

    @Entity
    @Table(name = "face_to_face_meetings")
    public static class FaceToFaceMeetingEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String shortListDecisionId;

        @Column(nullable = false)
        private String candidateId;

        @Column(nullable = false)
        private String projectId;

        @Column(nullable = false)
        private String location;

        @Column(nullable = false)
        private OffsetDateTime scheduledAt;

        @Column(nullable = false)
        private String agenda;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private MeetingStatus status;

        private Boolean agreementReached;

        private String outcome;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShortListDecisionId() {
            return shortListDecisionId;
        }

        public void setShortListDecisionId(String shortListDecisionId) {
            this.shortListDecisionId = shortListDecisionId;
        }

        public String getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(String candidateId) {
            this.candidateId = candidateId;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public OffsetDateTime getScheduledAt() {
            return scheduledAt;
        }

        public void setScheduledAt(OffsetDateTime scheduledAt) {
            this.scheduledAt = scheduledAt;
        }

        public String getAgenda() {
            return agenda;
        }

        public void setAgenda(String agenda) {
            this.agenda = agenda;
        }

        public MeetingStatus getStatus() {
            return status;
        }

        public void setStatus(MeetingStatus status) {
            this.status = status;
        }

        public Boolean getAgreementReached() {
            return agreementReached;
        }

        public void setAgreementReached(Boolean agreementReached) {
            this.agreementReached = agreementReached;
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }
    }

    @Entity
    @Table(name = "onboarding_workflows")
    public static class OnboardingWorkflowEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String candidateId;

        @Column(nullable = false)
        private String projectId;

        @Column(nullable = false)
        private String faceToFaceMeetingId;

        @Column(nullable = false)
        private String trainingProgramName;

        @Column(nullable = false)
        private LocalDate expectedCompletionDate;

        @Column(nullable = false)
        private int progress;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OnboardingStatus status;

        @Column(nullable = false)
        private boolean hrNotificationSent;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(String candidateId) {
            this.candidateId = candidateId;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getFaceToFaceMeetingId() {
            return faceToFaceMeetingId;
        }

        public void setFaceToFaceMeetingId(String faceToFaceMeetingId) {
            this.faceToFaceMeetingId = faceToFaceMeetingId;
        }

        public String getTrainingProgramName() {
            return trainingProgramName;
        }

        public void setTrainingProgramName(String trainingProgramName) {
            this.trainingProgramName = trainingProgramName;
        }

        public LocalDate getExpectedCompletionDate() {
            return expectedCompletionDate;
        }

        public void setExpectedCompletionDate(LocalDate expectedCompletionDate) {
            this.expectedCompletionDate = expectedCompletionDate;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public OnboardingStatus getStatus() {
            return status;
        }

        public void setStatus(OnboardingStatus status) {
            this.status = status;
        }

        public boolean isHrNotificationSent() {
            return hrNotificationSent;
        }

        public void setHrNotificationSent(boolean hrNotificationSent) {
            this.hrNotificationSent = hrNotificationSent;
        }
    }

    @Entity
    @Table(name = "notifications")
    public static class NotificationEntity {
        @Id
        private String id;

        @Column(nullable = false)
        private String recipient;

        @Column(nullable = false)
        private String message;

        @Column(nullable = false)
        private OffsetDateTime createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public OffsetDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}

// Made with Bob
