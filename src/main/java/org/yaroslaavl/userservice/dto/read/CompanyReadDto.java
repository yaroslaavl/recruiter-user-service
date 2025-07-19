package org.yaroslaavl.userservice.dto.read;

import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;

import java.time.LocalDateTime;

public record CompanyReadDto(String name,
                             String voivodeship,
                             String city,
                             String postCode,
                             String street,
                             Integer employeeCount,
                             String website,
                             String description,
                             String logoUrl,
                             String bannerUrl,
                             CompanyStatus companyStatus,
                             LocalDateTime createdAt) {
}
