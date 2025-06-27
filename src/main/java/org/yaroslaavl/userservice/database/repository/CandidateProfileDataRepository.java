package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.CandidateProfileData;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateProfileDataRepository extends JpaRepository<CandidateProfileData, UUID> {

     Optional<CandidateProfileData> findCandidateProfileDataByCandidateId(UUID candidateId);

}
