package org.yaroslaavl.userservice.database.entity.enums.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "CompanyRole")
public enum CompanyRole {
    ADMIN_RECRUITER("ADMIN_RECRUITER"),
    RECRUITER("RECRUITER");

    private final String companyRole;

    CompanyRole(String companyRole) {
        this.companyRole = companyRole;
    }
}
