package org.yaroslaavl.userservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;

import java.util.UUID;

public record CurrentUser(
        @NotNull UUID id,
        @NotBlank String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull AccountStatus accountStatus,
        @NotNull Boolean isTemporaryBlocked
) { }
