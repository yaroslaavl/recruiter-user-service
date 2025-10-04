package org.yaroslaavl.userservice.database.entity.enums.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AccountStatus")
public enum AccountStatus {

    PROFILE_INCOMPLETE,

    PROFILE_COMPLETE,

    PENDING_APPROVAL,

    REJECTED,

    APPROVED
}
