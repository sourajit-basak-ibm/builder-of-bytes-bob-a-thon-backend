package com.ibm.candidateonboarding.persistence;

import com.ibm.candidateonboarding.persistence.WorkflowEntities.OnboardingWorkflowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingWorkflowRepository extends JpaRepository<OnboardingWorkflowEntity, String> {
}

// Made with Bob
