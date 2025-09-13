package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.CandidateLanguage;

import java.util.Set;
import java.util.UUID;

@Repository
public interface LanguageRepository extends JpaRepository<CandidateLanguage, UUID> {

    @Modifying
    @Query("DELETE FROM CandidateLanguage cl WHERE cl.id NOT IN (:uuids) AND cl.candidate.email = :userEmail")
    void deleteAllUserLanguagesWhereIdsIsNotLike(@Param("uuids") Set<UUID> uuids, @Param("userEmail") String userEmail);
}
