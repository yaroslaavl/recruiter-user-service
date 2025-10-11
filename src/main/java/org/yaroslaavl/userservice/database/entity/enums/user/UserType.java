package org.yaroslaavl.userservice.database.entity.enums.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "UserType")
public enum UserType {
    CANDIDATE("ROLE_VERIFIED_CANDIDATE"),
    RECRUITER("ROLE_VERIFIED_RECRUITER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN");

    private final String keycloakRole;

    UserType(String keycloakRole) {
        this.keycloakRole = keycloakRole;
    }

    public List<String> getAllKeycloakRoles() {
        List<String> roles = new ArrayList<>();
        roles.add(keycloakRole);

        if (this == ADMIN) {
            roles.add(MANAGER.getKeycloakRole());
        }

        if (this == MANAGER || this == ADMIN) {
            roles.add(RECRUITER.getKeycloakRole());
        }

        if (this == MANAGER || this == ADMIN) {
            roles.add(CANDIDATE.getKeycloakRole());
        }

        return roles;
    }
}
