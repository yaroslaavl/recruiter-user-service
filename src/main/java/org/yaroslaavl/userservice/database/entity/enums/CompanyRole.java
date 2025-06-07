package org.yaroslaavl.userservice.database.entity.enums;

import lombok.Getter;

@Getter
public enum CompanyRole {
    ADMIN_RECRUITER("ADMIN_RECRUITER"),
    RECRUITER("RECRUITER");

    private final String companyRole;

    CompanyRole(String companyRole) {
        this.companyRole = companyRole;
    }
}
