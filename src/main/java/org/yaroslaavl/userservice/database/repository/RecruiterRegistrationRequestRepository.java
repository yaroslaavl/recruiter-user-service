package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;

import java.util.UUID;

@Repository
public interface RecruiterRegistrationRequestRepository extends JpaRepository<RecruiterRegistrationRequest, UUID> {
}
