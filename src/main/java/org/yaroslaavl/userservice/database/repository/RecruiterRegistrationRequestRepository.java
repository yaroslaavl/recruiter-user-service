package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecruiterRegistrationRequestRepository extends JpaRepository<RecruiterRegistrationRequest, UUID> {

    boolean existsByRecruiterId(UUID recruiterId);

    Optional<RecruiterRegistrationRequest> findByIdAndRequestStatus(UUID requestId, RequestStatus requestStatus);

    @Query("""
    SELECT rrr FROM RecruiterRegistrationRequest rrr
    WHERE (:status IS NULL OR rrr.requestStatus = :status)
    AND (:requestDateFrom IS NULL OR rrr.createdAt >= :requestDateFrom)
    ORDER BY rrr.createdAt ASC
    """)
    Page<RecruiterRegistrationRequest> getFilteredRequests(RequestStatus status, LocalDateTime requestDateFrom, Pageable pageable);
}
