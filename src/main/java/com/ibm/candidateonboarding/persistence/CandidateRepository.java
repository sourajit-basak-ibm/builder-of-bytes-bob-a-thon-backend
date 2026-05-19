package com.ibm.candidateonboarding.persistence;

import com.ibm.candidateonboarding.persistence.WorkflowEntities.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<CandidateEntity, String> {
    boolean existsByEmailIgnoreCase(String email);
}

// Made with Bob
