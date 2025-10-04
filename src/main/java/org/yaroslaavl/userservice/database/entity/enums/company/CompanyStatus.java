package org.yaroslaavl.userservice.database.entity.enums.company;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "CompanyStatus")
public enum CompanyStatus {

    APPROVED,

    REJECTED,

    PENDING,
    
    FROZEN
}
