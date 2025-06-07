package org.yaroslaavl.userservice.dto.read;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruiterRegistrationRequestReadDto(UUID recruiterId,
                                                  UUID companyId,
                                                  String requestStatus,
                                                  UUID reviewedBy,
                                                  LocalDateTime reviewedAt,
                                                  LocalDateTime createdAt) {
}
