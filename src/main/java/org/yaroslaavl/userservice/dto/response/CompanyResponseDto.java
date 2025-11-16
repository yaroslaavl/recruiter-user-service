package org.yaroslaavl.userservice.dto.response;

import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;

import java.time.LocalDateTime;

public record CompanyResponseDto(String name,
                                 String voivodeship,
                                 String city,
                                 String postCode,
                                 String street,
                                 Integer employeeCount,
                                 String website,
                                 String description,
                                 String logoUrl,
                                 String bannerUrl,
                                 Long vacancies,
                                 CompanyStatus companyStatus,
                                 LocalDateTime createdAt) {
}
