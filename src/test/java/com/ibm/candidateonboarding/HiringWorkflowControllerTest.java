package com.ibm.candidateonboarding;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HiringWorkflowControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Test
    void shouldRunEndToEndWorkflow() throws Exception {
        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Apollo",
                                  "technologyStack": ["Java", "Spring Boot"],
                                  "status": "PLANNING",
                                  "startDate": "2026-05-20",
                                  "commitments": "Build hiring platform",
                                  "managerName": "Anita Manager"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apollo"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectId = JsonTestUtils.read(projectResponse, "id");

        String requestResponse = mockMvc.perform(post("/api/staffing-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "projectId": "%s",
                                  "numberOfPositions": 2,
                                  "urgency": "HIGH",
                                  "skills": [
                                    {
                                      "name": "Java",
                                      "proficiency": "ADVANCED",
                                      "minimumYearsOfExperience": 3,
                                      "mandatory": true
                                    },
                                    {
                                      "name": "Spring Boot",
                                      "proficiency": "INTERMEDIATE",
                                      "minimumYearsOfExperience": 2,
                                      "mandatory": false
                                    }
                                  ]
                                }
                                """.formatted(projectId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RequestOpen"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String staffingRequestId = JsonTestUtils.read(requestResponse, "id");

        String candidateResponse = mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Riya Sen",
                                  "email": "riya@example.com",
                                  "phone": "9999999999",
                                  "source": "EXTERNAL",
                                  "skills": [
                                    {
                                      "name": "Java",
                                      "proficiency": "EXPERT",
                                      "minimumYearsOfExperience": 5,
                                      "mandatory": true
                                    },
                                    {
                                      "name": "Spring Boot",
                                      "proficiency": "ADVANCED",
                                      "minimumYearsOfExperience": 4,
                                      "mandatory": false
                                    }
                                  ],
                                  "resume": {
                                    "sourceName": "LinkedIn",
                                    "sourceType": "EXTERNAL",
                                    "url": "https://example.com/resume/riya",
                                    "lastUpdated": "2026-05-19T10:00:00Z",
                                    "summary": "Senior backend engineer",
                                    "totalExperienceYears": 6
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CandidateSourced"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String candidateId = JsonTestUtils.read(candidateResponse, "id");

        mockMvc.perform(get("/api/staffing-requests/{id}/matches", staffingRequestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].candidateId").value(candidateId));

        String interviewResponse = mockMvc.perform(post("/api/interviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "candidateId": "%s",
                                  "staffingRequestId": "%s",
                                  "scheduledAt": "2026-06-01T10:00:00Z",
                                  "interviewType": "TECHNICAL",
                                  "panelMembers": ["Lead Engineer"],
                                  "durationMinutes": 60
                                }
                                """.formatted(candidateId, staffingRequestId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("InterviewScheduled"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String interviewId = JsonTestUtils.read(interviewResponse, "id");

        mockMvc.perform(patch("/api/interviews/{id}/complete", interviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "feedback": "Strong system design and Java knowledge",
                                  "overallScore": 88
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("InterviewCompleted"));

        String decisionResponse = mockMvc.perform(post("/api/shortlist-decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "interviewId": "%s",
                                  "candidateId": "%s",
                                  "status": "selected",
                                  "reasoning": "Excellent match",
                                  "overallScore": 88
                                }
                                """.formatted(interviewId, candidateId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("selected"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String decisionId = JsonTestUtils.read(decisionResponse, "id");

        String meetingResponse = mockMvc.perform(post("/api/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "shortListDecisionId": "%s",
                                  "candidateId": "%s",
                                  "projectId": "%s",
                                  "location": "Bangalore Office",
                                  "scheduledAt": "2026-06-03T11:00:00Z",
                                  "agenda": "Project overview and commitments"
                                }
                                """.formatted(decisionId, candidateId, projectId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("MeetingScheduled"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String meetingId = JsonTestUtils.read(meetingResponse, "id");

        mockMvc.perform(patch("/api/meetings/{id}/complete", meetingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "agreementReached": true,
                                  "outcome": "Candidate accepted terms"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("MeetingCompleted"));

        mockMvc.perform(post("/api/onboardings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "candidateId": "%s",
                                  "projectId": "%s",
                                  "faceToFaceMeetingId": "%s",
                                  "trainingProgramName": "Platform Onboarding",
                                  "expectedCompletionDate": "2026-06-20"
                                }
                                """.formatted(candidateId, projectId, meetingId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OnboardingInitiated"))
                .andExpect(jsonPath("$.hrNotificationSent").value(true));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipient").exists());
    }

    @Test
    void shouldRejectShortlistDecisionWithoutCompletedInterview() throws Exception {
        mockMvc.perform(post("/api/shortlist-decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "interviewId": "missing",
                                  "candidateId": "missing",
                                  "status": "selected",
                                  "reasoning": "Invalid flow",
                                  "overallScore": 80
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}

// Made with Bob
