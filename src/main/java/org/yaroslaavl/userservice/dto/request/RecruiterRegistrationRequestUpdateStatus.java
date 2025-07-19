package org.yaroslaavl.userservice.dto.request;

import lombok.Getter;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;

import java.util.UUID;

@Getter
public class RecruiterRegistrationRequestUpdateStatus {

    private UUID registrationRequestId;

    private RequestStatus requestStatus;
}
