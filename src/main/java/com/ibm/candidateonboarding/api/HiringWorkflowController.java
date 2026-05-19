package com.ibm.candidateonboarding.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.candidateonboarding.api.ApiModels.CandidateCreateRequest;
import com.ibm.candidateonboarding.api.ApiModels.CandidateResponse;
import com.ibm.candidateonboarding.api.ApiModels.FaceToFaceMeetingCompleteRequest;
import com.ibm.candidateonboarding.api.ApiModels.FaceToFaceMeetingRequest;
import com.ibm.candidateonboarding.api.ApiModels.FaceToFaceMeetingResponse;
import com.ibm.candidateonboarding.api.ApiModels.HealthResponse;
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
import com.ibm.candidateonboarding.api.ApiModels.ShortlistDecisionRequest;
import com.ibm.candidateonboarding.api.ApiModels.ShortlistDecisionResponse;
import com.ibm.candidateonboarding.api.ApiModels.StaffingRequestCreateRequest;
import com.ibm.candidateonboarding.api.ApiModels.StaffingRequestResponse;
import com.ibm.candidateonboarding.service.HiringWorkflowService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class HiringWorkflowController {

    private final HiringWorkflowService service;

    public HiringWorkflowController(HiringWorkflowService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse("UP", "candidate-onboarding");
    }

    @PostMapping("/projects")
    public ProjectResponse createProject(@Valid @RequestBody ProjectRequest request) {
        return service.createProject(request);
    }

    @GetMapping("/projects")
    public List<ProjectResponse> listProjects() {
        return service.listProjects();
    }

    @PostMapping("/staffing-requests")
    public StaffingRequestResponse createStaffingRequest(@Valid @RequestBody StaffingRequestCreateRequest request) {
        return service.createStaffingRequest(request);
    }

    @GetMapping("/staffing-requests")
    public List<StaffingRequestResponse> listStaffingRequests() {
        return service.listStaffingRequests();
    }

    @GetMapping("/staffing-requests/{id}/matches")
    public List<MatchResponse> matchCandidates(@PathVariable String id) {
        return service.matchCandidates(id);
    }

    @PostMapping("/candidates")
    public CandidateResponse createCandidate(@Valid @RequestBody CandidateCreateRequest request) {
        return service.createCandidate(request);
    }

    @GetMapping("/candidates")
    public List<CandidateResponse> listCandidates() {
        return service.listCandidates();
    }

    @PostMapping("/interviews")
    public InterviewResponse scheduleInterview(@Valid @RequestBody InterviewScheduleRequest request) {
        return service.scheduleInterview(request);
    }

    @PatchMapping("/interviews/{id}/complete")
    public InterviewResponse completeInterview(@PathVariable String id, @Valid @RequestBody InterviewCompleteRequest request) {
        return service.completeInterview(id, request);
    }

    @GetMapping("/interviews")
    public List<InterviewResponse> listInterviews() {
        return service.listInterviews();
    }

    @PostMapping("/shortlist-decisions")
    public ShortlistDecisionResponse createDecision(@Valid @RequestBody ShortlistDecisionRequest request) {
        return service.createShortlistDecision(request);
    }

    @GetMapping("/shortlist-decisions")
    public List<ShortlistDecisionResponse> listDecisions() {
        return service.listShortlistDecisions();
    }

    @PostMapping("/meetings")
    public FaceToFaceMeetingResponse scheduleMeeting(@Valid @RequestBody FaceToFaceMeetingRequest request) {
        return service.scheduleMeeting(request);
    }

    @PatchMapping("/meetings/{id}/complete")
    public FaceToFaceMeetingResponse completeMeeting(@PathVariable String id, @Valid @RequestBody FaceToFaceMeetingCompleteRequest request) {
        return service.completeMeeting(id, request);
    }

    @GetMapping("/meetings")
    public List<FaceToFaceMeetingResponse> listMeetings() {
        return service.listMeetings();
    }

    @PostMapping("/onboardings")
    public OnboardingWorkflowResponse initiateOnboarding(@Valid @RequestBody OnboardingInitiateRequest request) {
        return service.initiateOnboarding(request);
    }

    @PatchMapping("/onboardings/{id}/progress")
    public OnboardingWorkflowResponse updateOnboarding(@PathVariable String id, @Valid @RequestBody OnboardingProgressRequest request) {
        return service.updateOnboardingProgress(id, request);
    }

    @GetMapping("/onboardings")
    public List<OnboardingWorkflowResponse> listOnboardings() {
        return service.listOnboardings();
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> listNotifications() {
        return service.listNotifications();
    }
}

// Made with Bob
