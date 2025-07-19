package org.yaroslaavl.userservice.dto.read;

import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruiterRegistrationRequestReadDto(UUID recruiterId,
                                                  UUID companyId,
                                                  RequestStatus requestStatus,
                                                  UUID reviewedBy,
                                                  LocalDateTime reviewedAt,
                                                  LocalDateTime createdAt) {
}
