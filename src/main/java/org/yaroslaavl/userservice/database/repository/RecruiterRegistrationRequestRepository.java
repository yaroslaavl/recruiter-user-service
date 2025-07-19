package org.yaroslaavl.userservice.database.repository;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;
import org.yaroslaavl.userservice.dto.request.RecruiterRegistrationRequestUpdateStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecruiterRegistrationRequestRepository extends JpaRepository<RecruiterRegistrationRequest, UUID> {

    boolean existsByRecruiterId(UUID recruiterId);

    Optional<RecruiterRegistrationRequest> findByIdAndRequestStatus(UUID requestId, RequestStatus requestStatus);
}
