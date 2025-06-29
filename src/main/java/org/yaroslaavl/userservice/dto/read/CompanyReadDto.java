package org.yaroslaavl.userservice.dto.read;

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
                             String companyStatus,
                             LocalDateTime createdAt) {
}
