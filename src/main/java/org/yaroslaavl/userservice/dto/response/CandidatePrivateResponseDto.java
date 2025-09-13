package org.yaroslaavl.userservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.user.UserType;

import java.time.LocalDateTime;

public record CandidatePrivateResponseDto(
        @NotBlank String email,
        @NotBlank String displayName,
        @NotNull UserType userType,
        @NotNull AccountStatus accountStatus,
        @NotBlank String phoneNumber,
        @NotNull LocalDateTime createdAt,
        String linkedinLink,
        CandidateProfileDataResponseDto candidateProfileDataResponseDto
) { }
