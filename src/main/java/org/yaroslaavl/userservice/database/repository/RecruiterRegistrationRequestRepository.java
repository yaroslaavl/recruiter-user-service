package org.yaroslaavl.userservice.database.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph(attributePaths = {"recruiter", "reviewedBy", "company"})
    @Query(value = """
    SELECT rrr FROM RecruiterRegistrationRequest rrr
    WHERE (:status IS NULL OR rrr.requestStatus = :status)
    AND (rrr.createdAt BETWEEN :selectedDateStart AND :selectedDateEnd)
    ORDER BY rrr.createdAt DESC
    """, countQuery = """
    SELECT COUNT(rrr) FROM RecruiterRegistrationRequest rrr
    WHERE (:status IS NULL OR rrr.requestStatus = :status)
    AND (rrr.createdAt BETWEEN :selectedDateStart AND :selectedDateEnd)
    """)
    Page<RecruiterRegistrationRequest> getFilteredRequests(RequestStatus status,
                                                           @NotNull LocalDateTime selectedDateStart,
                                                           @NotNull LocalDateTime selectedDateEnd,
                                                           Pageable pageable);
}
