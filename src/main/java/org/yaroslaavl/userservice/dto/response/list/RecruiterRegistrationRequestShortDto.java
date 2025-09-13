package org.yaroslaavl.userservice.dto.response.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruiterRegistrationRequestShortDto(
        @NotNull UUID id,
        @NotBlank String recruiterEmail,
        @NotBlank String companyName,
        @NotNull RequestStatus requestStatus,
        String reviewedBy,
        LocalDateTime createdAt
) { }
