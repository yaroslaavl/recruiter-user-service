package org.yaroslaavl.userservice.feignClient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserShortDto(
        @NotNull String userId,
        @NotBlank String email,
        @NotBlank String fullName
) { }
