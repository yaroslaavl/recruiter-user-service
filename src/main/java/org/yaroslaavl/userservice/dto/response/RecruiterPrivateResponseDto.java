package org.yaroslaavl.userservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyRole;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.user.UserType;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruiterPrivateResponseDto(
        @NotBlank String email,
        @NotBlank String displayName,
        @NotNull UserType userType,
        @NotNull AccountStatus accountStatus,
        @NotBlank String position,
        @NotNull UUID companyId,
        @NotBlank String companyName,
        @NotNull CompanyRole companyRole,
        @NotNull LocalDateTime createdAt
) { }
