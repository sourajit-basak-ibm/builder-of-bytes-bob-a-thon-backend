package com.ibm.candidateonboarding.persistence;

import com.ibm.candidateonboarding.persistence.WorkflowEntities.ShortlistDecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortlistDecisionRepository extends JpaRepository<ShortlistDecisionEntity, String> {
}

// Made with Bob
