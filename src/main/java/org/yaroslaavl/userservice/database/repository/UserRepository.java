package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.User;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByKeycloakId(String keycloakId);

    @Query("""
    SELECT u FROM User u
    WHERE u.keycloakId IN (:userKeycloakIds)
    """)
    List<User> findUsersByUserKeycloakId(Set<String> userKeycloakIds);
}
