package com.ibm.candidateonboarding.persistence;

import com.ibm.candidateonboarding.persistence.WorkflowEntities.StaffingRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffingRequestRepository extends JpaRepository<StaffingRequestEntity, String> {
}

// Made with Bob
