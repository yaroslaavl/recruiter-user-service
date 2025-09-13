package org.yaroslaavl.userservice.dto.response;

import jakarta.validation.constraints.NotBlank;

public record RecruiterPublicResponseDto(
        @NotBlank String displayName,
        @NotBlank String position,
        @NotBlank String companyName
) { }
