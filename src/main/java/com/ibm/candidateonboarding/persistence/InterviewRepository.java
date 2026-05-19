package com.ibm.candidateonboarding.persistence;

import com.ibm.candidateonboarding.persistence.WorkflowEntities.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<InterviewEntity, String> {
}

// Made with Bob
