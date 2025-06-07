package org.yaroslaavl.userservice.dto.read;

import java.time.LocalDateTime;

public record CompanyReadDto(String name,
                             String voivodeship,
                             String city,
                             String postCode,
                             String street,
                             Integer employeeCount,
                             String description,
                             String companyStatus,
                             LocalDateTime createdAt) {
}
