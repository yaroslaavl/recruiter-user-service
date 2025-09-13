package org.yaroslaavl.userservice.dto.response.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompanyShortDto(
        @NotNull UUID id,
        @NotBlank String name,
        String logoUrl,
        String description,
        Long vacancies,
        Integer employeeCount
) { }
