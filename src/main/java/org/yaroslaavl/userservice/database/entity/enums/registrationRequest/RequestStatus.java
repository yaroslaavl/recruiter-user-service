package org.yaroslaavl.userservice.database.entity.enums.registrationRequest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "RequestStatus")
public enum RequestStatus {

    PENDING,

    APPROVED,

    REJECTED
}
