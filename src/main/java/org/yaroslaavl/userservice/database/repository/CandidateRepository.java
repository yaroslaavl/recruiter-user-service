package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    @Query("""
    SELECT c FROM Candidate c
    JOIN c.profileData data
    WHERE (:salary IS NULL OR data.desiredSalary >= :salary)
    AND (:workMode IS NULL OR data.workMode = :workMode)
    AND (:availableHoursPerWeek IS NULL OR data.availableHoursPerWeek = :availableHoursPerWeek)
    AND (:availableFrom IS NULL OR data.availableFrom = :availableFrom)
    """)
    List<Candidate> getFilteredCandidates(Salary salary, WorkMode workMode, Integer availableHoursPerWeek, AvailableFrom availableFrom);

    Optional<Candidate> findByEmail(String email);

    Candidate findByKeycloakId(String keycloakId);
}
