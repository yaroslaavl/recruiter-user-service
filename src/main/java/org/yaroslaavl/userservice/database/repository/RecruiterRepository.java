package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.Recruiter;

import java.util.UUID;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, UUID> {
}
