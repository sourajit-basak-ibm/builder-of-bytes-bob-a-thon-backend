package com.ibm.candidateonboarding.service;

import com.ibm.candidateonboarding.api.ApiModels.CandidateCreateRequest;
import com.ibm.candidateonboarding.api.ApiModels.CandidateResponse;
import com.ibm.candidateonboarding.api.ApiModels.FaceToFaceMeetingCompleteRequest;
import com.ibm.candidateonboarding.api.ApiModels.FaceToFaceMeetingRequest;
import com.ibm.candidateonboarding.api.ApiModels.FaceToFaceMeetingResponse;
import com.ibm.candidateonboarding.api.ApiModels.InterviewCompleteRequest;
import com.ibm.candidateonboarding.api.ApiModels.InterviewResponse;
import com.ibm.candidateonboarding.api.ApiModels.InterviewScheduleRequest;
import com.ibm.candidateonboarding.api.ApiModels.MatchResponse;
import com.ibm.candidateonboarding.api.ApiModels.NotificationResponse;
import com.ibm.candidateonboarding.api.ApiModels.OnboardingInitiateRequest;
import com.ibm.candidateonboarding.api.ApiModels.OnboardingProgressRequest;
import com.ibm.candidateonboarding.api.ApiModels.OnboardingWorkflowResponse;
import com.ibm.candidateonboarding.api.ApiModels.ProjectRequest;
import com.ibm.candidateonboarding.api.ApiModels.ProjectResponse;
import com.ibm.candidateonboarding.api.ApiModels.ResumeResponse;
import com.ibm.candidateonboarding.api.ApiModels.ShortlistDecisionRequest;
import com.ibm.candidateonboarding.api.ApiModels.ShortlistDecisionResponse;
import com.ibm.candidateonboarding.api.ApiModels.SkillRequest;
import com.ibm.candidateonboarding.api.ApiModels.SkillResponse;
import com.ibm.candidateonboarding.api.ApiModels.StaffingRequestCreateRequest;
import com.ibm.candidateonboarding.api.ApiModels.StaffingRequestResponse;
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
import com.ibm.candidateonboarding.persistence.CandidateRepository;
import com.ibm.candidateonboarding.persistence.FaceToFaceMeetingRepository;
import com.ibm.candidateonboarding.persistence.InterviewRepository;
import com.ibm.candidateonboarding.persistence.NotificationRepository;
import com.ibm.candidateonboarding.persistence.OnboardingWorkflowRepository;
import com.ibm.candidateonboarding.persistence.ProjectRepository;
import com.ibm.candidateonboarding.persistence.ShortlistDecisionRepository;
import com.ibm.candidateonboarding.persistence.StaffingRequestRepository;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.CandidateEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.FaceToFaceMeetingEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.InterviewEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.NotificationEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.OnboardingWorkflowEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.ProjectEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.ResumeEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.ShortlistDecisionEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.SkillEntity;
import com.ibm.candidateonboarding.persistence.WorkflowEntities.StaffingRequestEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class HiringWorkflowService {

    private final ProjectRepository projectRepository;
    private final StaffingRequestRepository staffingRequestRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private final ShortlistDecisionRepository shortlistDecisionRepository;
    private final FaceToFaceMeetingRepository faceToFaceMeetingRepository;
    private final OnboardingWorkflowRepository onboardingWorkflowRepository;
    private final NotificationRepository notificationRepository;
    private final String hrEmail;

    public HiringWorkflowService(
            ProjectRepository projectRepository,
            StaffingRequestRepository staffingRequestRepository,
            CandidateRepository candidateRepository,
            InterviewRepository interviewRepository,
            ShortlistDecisionRepository shortlistDecisionRepository,
            FaceToFaceMeetingRepository faceToFaceMeetingRepository,
            OnboardingWorkflowRepository onboardingWorkflowRepository,
            NotificationRepository notificationRepository,
            @Value("${app.notifications.hr-email}") String hrEmail
    ) {
        this.projectRepository = projectRepository;
        this.staffingRequestRepository = staffingRequestRepository;
        this.candidateRepository = candidateRepository;
        this.interviewRepository = interviewRepository;
        this.shortlistDecisionRepository = shortlistDecisionRepository;
        this.faceToFaceMeetingRepository = faceToFaceMeetingRepository;
        this.onboardingWorkflowRepository = onboardingWorkflowRepository;
        this.notificationRepository = notificationRepository;
        this.hrEmail = hrEmail;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        ensureUniqueProjectName(request.name());
        if (request.status() == ProjectStatus.ACTIVE) {
            validateProjectCanBeActivated(request.technologyStack(), request.managerName());
        }

        ProjectEntity entity = new ProjectEntity();
        entity.setId(newId("prj"));
        entity.setName(request.name());
        entity.setTechnologyStack(new ArrayList<>(request.technologyStack()));
        entity.setStatus(request.status());
        entity.setStartDate(request.startDate());
        entity.setCommitments(request.commitments());
        entity.setManagerName(request.managerName());

        return toProjectResponse(projectRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listProjects() {
        return projectRepository.findAll().stream()
                .map(this::toProjectResponse)
                .toList();
    }

    public StaffingRequestResponse createStaffingRequest(StaffingRequestCreateRequest request) {
        ProjectEntity project = getProject(request.projectId());
        if (!(project.getStatus() == ProjectStatus.PLANNING || project.getStatus() == ProjectStatus.ACTIVE)) {
            throw new IllegalArgumentException("Request must be associated with a planning or active project");
        }
        if (request.skills().isEmpty()) {
            throw new IllegalArgumentException("At least one skill requirement must be defined");
        }

        StaffingRequestEntity entity = new StaffingRequestEntity();
        entity.setId(newId("req"));
        entity.setProjectId(request.projectId());
        entity.setNumberOfPositions(request.numberOfPositions());
        entity.setUrgency(request.urgency());
        entity.setStatus(StaffingRequestStatus.RequestOpen);
        entity.setSkills(request.skills().stream().map(this::toSkillEntity).toList());

        return toStaffingRequestResponse(staffingRequestRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<StaffingRequestResponse> listStaffingRequests() {
        return staffingRequestRepository.findAll().stream()
                .map(this::toStaffingRequestResponse)
                .toList();
    }

    public CandidateResponse createCandidate(CandidateCreateRequest request) {
        ensureUniqueCandidateEmail(request.email());
        ensureResumeSourcePresent(request);

        CandidateEntity entity = new CandidateEntity();
        entity.setId(newId("cand"));
        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setSource(request.source());
        entity.setStatus(CandidateStatus.CandidateSourced);
        entity.setSkills(request.skills().stream().map(this::toSkillEntity).toList());
        entity.setResume(toResumeEntity(request));

        CandidateEntity saved = candidateRepository.save(entity);

        if (request.source() == CandidateSource.EXTERNAL) {
            createNotification(hrEmail, "External candidate registered: " + request.name());
        }

        return toCandidateResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CandidateResponse> listCandidates() {
        return candidateRepository.findAll().stream()
                .map(this::toCandidateResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MatchResponse> matchCandidates(String staffingRequestId) {
        StaffingRequestEntity request = getStaffingRequest(staffingRequestId);

        return candidateRepository.findAll().stream()
                .map(candidate -> buildMatchResponse(candidate, request))
                .sorted(Comparator.comparingInt(MatchResponse::score).reversed()
                        .thenComparing(match -> !match.internalPriority()))
                .toList();
    }

    public InterviewResponse scheduleInterview(InterviewScheduleRequest request) {
        CandidateEntity candidate = getCandidate(request.candidateId());
        getStaffingRequest(request.staffingRequestId());

        if (request.panelMembers().isEmpty()) {
            throw new IllegalArgumentException("Interview must have at least one panel member");
        }

        InterviewEntity entity = new InterviewEntity();
        entity.setId(newId("int"));
        entity.setCandidateId(candidate.getId());
        entity.setStaffingRequestId(request.staffingRequestId());
        entity.setScheduledAt(request.scheduledAt());
        entity.setInterviewType(request.interviewType());
        entity.setStatus(InterviewStatus.InterviewScheduled);
        entity.setPanelMembers(new ArrayList<>(request.panelMembers()));
        entity.setDurationMinutes(request.durationMinutes());
        entity.setTeamsLink("https://teams.local/meet/" + entity.getId());

        InterviewEntity saved = interviewRepository.save(entity);
        updateCandidateStatus(candidate.getId(), CandidateStatus.CandidateInterviewing);

        return toInterviewResponse(saved);
    }

    public InterviewResponse completeInterview(String interviewId, InterviewCompleteRequest request) {
        InterviewEntity entity = getInterview(interviewId);
        entity.setStatus(InterviewStatus.InterviewCompleted);
        entity.setFeedback(request.feedback());
        entity.setOverallScore(request.overallScore());
        return toInterviewResponse(interviewRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> listInterviews() {
        return interviewRepository.findAll().stream()
                .map(this::toInterviewResponse)
                .toList();
    }

    public ShortlistDecisionResponse createShortlistDecision(ShortlistDecisionRequest request) {
        InterviewEntity interview = getInterview(request.interviewId());
        if (interview.getStatus() != InterviewStatus.InterviewCompleted
                || interview.getFeedback() == null
                || interview.getFeedback().isBlank()) {
            throw new IllegalArgumentException("Shortlist decisions require completed interview feedback");
        }

        String normalizedStatus = request.status().trim().toLowerCase();
        if (!normalizedStatus.equals("selected") && !normalizedStatus.equals("rejected")) {
            throw new IllegalArgumentException("Shortlist status must be selected or rejected");
        }

        ShortlistDecisionEntity entity = new ShortlistDecisionEntity();
        entity.setId(newId("sd"));
        entity.setInterviewId(request.interviewId());
        entity.setCandidateId(request.candidateId());
        entity.setStatus(normalizedStatus);
        entity.setReasoning(request.reasoning());
        entity.setOverallScore(request.overallScore());
        entity.setNotificationTriggered(true);

        ShortlistDecisionEntity saved = shortlistDecisionRepository.save(entity);

        if (normalizedStatus.equals("selected")) {
            updateCandidateStatus(request.candidateId(), CandidateStatus.CandidateShortlisted);
        } else {
            updateCandidateStatus(request.candidateId(), CandidateStatus.CandidateRejected);
        }

        createNotification("project-manager@example.com", "Shortlist decision recorded for candidate " + request.candidateId());
        return toShortlistDecisionResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ShortlistDecisionResponse> listShortlistDecisions() {
        return shortlistDecisionRepository.findAll().stream()
                .map(this::toShortlistDecisionResponse)
                .toList();
    }

    public FaceToFaceMeetingResponse scheduleMeeting(FaceToFaceMeetingRequest request) {
        ShortlistDecisionEntity decision = getShortlistDecision(request.shortListDecisionId());
        if (!"selected".equals(decision.getStatus())) {
            throw new IllegalArgumentException("Only shortlisted candidates can have face-to-face meetings");
        }

        FaceToFaceMeetingEntity entity = new FaceToFaceMeetingEntity();
        entity.setId(newId("mtg"));
        entity.setShortListDecisionId(request.shortListDecisionId());
        entity.setCandidateId(request.candidateId());
        entity.setProjectId(request.projectId());
        entity.setLocation(request.location());
        entity.setScheduledAt(request.scheduledAt());
        entity.setAgenda(request.agenda());
        entity.setStatus(MeetingStatus.MeetingScheduled);

        return toMeetingResponse(faceToFaceMeetingRepository.save(entity));
    }

    public FaceToFaceMeetingResponse completeMeeting(String meetingId, FaceToFaceMeetingCompleteRequest request) {
        FaceToFaceMeetingEntity entity = getMeeting(meetingId);
        entity.setStatus(MeetingStatus.MeetingCompleted);
        entity.setAgreementReached(request.agreementReached());
        entity.setOutcome(request.outcome());
        return toMeetingResponse(faceToFaceMeetingRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<FaceToFaceMeetingResponse> listMeetings() {
        return faceToFaceMeetingRepository.findAll().stream()
                .map(this::toMeetingResponse)
                .toList();
    }

    public OnboardingWorkflowResponse initiateOnboarding(OnboardingInitiateRequest request) {
        FaceToFaceMeetingEntity meeting = getMeeting(request.faceToFaceMeetingId());
        if (meeting.getStatus() != MeetingStatus.MeetingCompleted || !Boolean.TRUE.equals(meeting.getAgreementReached())) {
            throw new IllegalArgumentException("Onboarding needs a completed face-to-face meeting with agreement reached");
        }

        CandidateEntity candidate = getCandidate(request.candidateId());
        updateCandidateStatus(candidate.getId(), CandidateStatus.CandidateHired);

        boolean hrNotificationSent = candidate.getSource() == CandidateSource.EXTERNAL;
        if (hrNotificationSent) {
            createNotification(hrEmail, "Onboarding initiated for external candidate " + candidate.getName());
        }

        OnboardingWorkflowEntity entity = new OnboardingWorkflowEntity();
        entity.setId(newId("onb"));
        entity.setCandidateId(request.candidateId());
        entity.setProjectId(request.projectId());
        entity.setFaceToFaceMeetingId(request.faceToFaceMeetingId());
        entity.setTrainingProgramName(request.trainingProgramName());
        entity.setExpectedCompletionDate(request.expectedCompletionDate());
        entity.setProgress(0);
        entity.setStatus(OnboardingStatus.OnboardingInitiated);
        entity.setHrNotificationSent(hrNotificationSent);

        return toOnboardingResponse(onboardingWorkflowRepository.save(entity));
    }

    public OnboardingWorkflowResponse updateOnboardingProgress(String onboardingId, OnboardingProgressRequest request) {
        OnboardingWorkflowEntity entity = getOnboarding(onboardingId);
        entity.setProgress(request.progress());
        entity.setStatus(request.status());
        return toOnboardingResponse(onboardingWorkflowRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<OnboardingWorkflowResponse> listOnboardings() {
        return onboardingWorkflowRepository.findAll().stream()
                .map(this::toOnboardingResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> listNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::toNotificationResponse)
                .toList();
    }

    private MatchResponse buildMatchResponse(CandidateEntity candidate, StaffingRequestEntity request) {
        boolean mandatoryMatched = request.getSkills().stream()
                .filter(SkillEntity::isMandatory)
                .allMatch(required -> candidate.getSkills().stream().anyMatch(skill -> skillMatches(required, skill)));

        int score = 0;
        for (SkillEntity required : request.getSkills()) {
            SkillEntity matchedSkill = candidate.getSkills().stream()
                    .filter(skill -> skillMatches(required, skill))
                    .findFirst()
                    .orElse(null);
            if (matchedSkill != null) {
                score += required.isMandatory() ? 35 : 15;
                score += proficiencyBonus(matchedSkill.getProficiency());
            }
        }

        if (candidate.getSource() == CandidateSource.INTERNAL) {
            score += 10;
        }

        score = Math.min(score, 100);
        boolean autoConsidered = mandatoryMatched && score > 70;
        return new MatchResponse(
                candidate.getId(),
                candidate.getName(),
                score,
                mandatoryMatched,
                autoConsidered,
                candidate.getSource() == CandidateSource.INTERNAL
        );
    }

    private boolean skillMatches(SkillEntity required, SkillEntity actual) {
        boolean sameName = required.getName().equalsIgnoreCase(actual.getName());
        boolean proficiencyEnough = actual.getProficiency().ordinal() >= required.getProficiency().ordinal();
        boolean yearsEnough = required.getMinimumYearsOfExperience() == null
                || actual.getMinimumYearsOfExperience() == null
                || actual.getMinimumYearsOfExperience() >= required.getMinimumYearsOfExperience();
        return sameName && proficiencyEnough && yearsEnough;
    }

    private int proficiencyBonus(SkillProficiency proficiency) {
        return switch (proficiency) {
            case BEGINNER -> 2;
            case INTERMEDIATE -> 5;
            case ADVANCED -> 8;
            case EXPERT -> 10;
        };
    }

    private void validateProjectCanBeActivated(List<String> technologyStack, String managerName) {
        if (technologyStack == null || technologyStack.isEmpty()) {
            throw new IllegalArgumentException("Technology stack must be defined before project activation");
        }
        if (managerName == null || managerName.isBlank()) {
            throw new IllegalArgumentException("Project cannot move to ACTIVE state without an assigned manager");
        }
    }

    private void ensureUniqueProjectName(String name) {
        if (projectRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Project name must be unique");
        }
    }

    private void ensureUniqueCandidateEmail(String email) {
        if (candidateRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Candidate email must be unique");
        }
    }

    private void ensureResumeSourcePresent(CandidateCreateRequest request) {
        if (request.resume() == null || request.resume().sourceName().isBlank() || request.resume().url().isBlank()) {
            throw new IllegalArgumentException("Each candidate must have exactly one resume tied to a source and URL");
        }
    }

    private void updateCandidateStatus(String candidateId, CandidateStatus status) {
        CandidateEntity candidate = getCandidate(candidateId);
        candidate.setStatus(status);
        candidateRepository.save(candidate);
    }

    private NotificationEntity createNotification(String recipient, String message) {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(newId("ntf"));
        entity.setRecipient(recipient);
        entity.setMessage(message);
        entity.setCreatedAt(OffsetDateTime.now());
        return notificationRepository.save(entity);
    }

    private ProjectEntity getProject(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    private StaffingRequestEntity getStaffingRequest(String id) {
        return staffingRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staffing request not found: " + id));
    }

    private CandidateEntity getCandidate(String id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
    }

    private InterviewEntity getInterview(String id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found: " + id));
    }

    private ShortlistDecisionEntity getShortlistDecision(String id) {
        return shortlistDecisionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shortlist decision not found: " + id));
    }

    private FaceToFaceMeetingEntity getMeeting(String id) {
        return faceToFaceMeetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found: " + id));
    }

    private OnboardingWorkflowEntity getOnboarding(String id) {
        return onboardingWorkflowRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Onboarding workflow not found: " + id));
    }

    private String newId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private SkillEntity toSkillEntity(SkillRequest request) {
        SkillEntity entity = new SkillEntity();
        entity.setId(newId("sk"));
        entity.setName(request.name());
        entity.setProficiency(request.proficiency());
        entity.setMinimumYearsOfExperience(request.minimumYearsOfExperience());
        entity.setMandatory(request.mandatory());
        return entity;
    }

    private ResumeEntity toResumeEntity(CandidateCreateRequest request) {
        ResumeEntity entity = new ResumeEntity();
        entity.setId(newId("res"));
        entity.setSourceName(request.resume().sourceName());
        entity.setSourceType(request.resume().sourceType());
        entity.setUrl(request.resume().url());
        entity.setLastUpdated(request.resume().lastUpdated());
        entity.setSummary(request.resume().summary());
        entity.setTotalExperienceYears(request.resume().totalExperienceYears());
        return entity;
    }

    private ProjectResponse toProjectResponse(ProjectEntity entity) {
        return new ProjectResponse(
                entity.getId(),
                entity.getName(),
                entity.getTechnologyStack(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getCommitments(),
                entity.getManagerName()
        );
    }

    private StaffingRequestResponse toStaffingRequestResponse(StaffingRequestEntity entity) {
        return new StaffingRequestResponse(
                entity.getId(),
                entity.getProjectId(),
                entity.getNumberOfPositions(),
                entity.getUrgency(),
                entity.getStatus(),
                entity.getSkills().stream().map(this::toSkillResponse).toList()
        );
    }

    private CandidateResponse toCandidateResponse(CandidateEntity entity) {
        return new CandidateResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getSource(),
                entity.getStatus(),
                entity.getSkills().stream().map(this::toSkillResponse).toList(),
                new ResumeResponse(
                        entity.getResume().getSourceName(),
                        entity.getResume().getSourceType(),
                        entity.getResume().getUrl(),
                        entity.getResume().getLastUpdated(),
                        entity.getResume().getSummary(),
                        entity.getResume().getTotalExperienceYears()
                )
        );
    }

    private InterviewResponse toInterviewResponse(InterviewEntity entity) {
        return new InterviewResponse(
                entity.getId(),
                entity.getCandidateId(),
                entity.getStaffingRequestId(),
                entity.getScheduledAt(),
                entity.getInterviewType(),
                entity.getStatus(),
                entity.getPanelMembers(),
                entity.getDurationMinutes(),
                entity.getTeamsLink(),
                entity.getFeedback(),
                entity.getOverallScore()
        );
    }

    private ShortlistDecisionResponse toShortlistDecisionResponse(ShortlistDecisionEntity entity) {
        return new ShortlistDecisionResponse(
                entity.getId(),
                entity.getInterviewId(),
                entity.getCandidateId(),
                entity.getStatus(),
                entity.getReasoning(),
                entity.getOverallScore(),
                entity.isNotificationTriggered()
        );
    }

    private FaceToFaceMeetingResponse toMeetingResponse(FaceToFaceMeetingEntity entity) {
        return new FaceToFaceMeetingResponse(
                entity.getId(),
                entity.getShortListDecisionId(),
                entity.getCandidateId(),
                entity.getProjectId(),
                entity.getLocation(),
                entity.getScheduledAt(),
                entity.getAgenda(),
                entity.getStatus(),
                entity.getAgreementReached(),
                entity.getOutcome()
        );
    }

    private OnboardingWorkflowResponse toOnboardingResponse(OnboardingWorkflowEntity entity) {
        return new OnboardingWorkflowResponse(
                entity.getId(),
                entity.getCandidateId(),
                entity.getProjectId(),
                entity.getFaceToFaceMeetingId(),
                entity.getTrainingProgramName(),
                entity.getExpectedCompletionDate(),
                entity.getProgress(),
                entity.getStatus(),
                entity.isHrNotificationSent()
        );
    }

    private NotificationResponse toNotificationResponse(NotificationEntity entity) {
        return new NotificationResponse(
                entity.getId(),
                entity.getRecipient(),
                entity.getMessage(),
                entity.getCreatedAt()
        );
    }

    private SkillResponse toSkillResponse(SkillEntity entity) {
        return new SkillResponse(
                entity.getId(),
                entity.getName(),
                entity.getProficiency(),
                entity.getMinimumYearsOfExperience(),
                entity.isMandatory()
        );
    }
}

// Made with Bob
