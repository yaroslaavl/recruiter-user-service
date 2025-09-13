package org.yaroslaavl.userservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruiterRegistrationRequestResponseDto(@NotNull UUID recruiterId,
                                                      @NotBlank String companyName,
                                                      @NotNull CompanyStatus companyStatus,
                                                      @NotNull RequestStatus requestStatus,
                                                      String reviewedBy,
                                                      LocalDateTime reviewedAt,
                                                      @NotNull LocalDateTime createdAt) {
}
