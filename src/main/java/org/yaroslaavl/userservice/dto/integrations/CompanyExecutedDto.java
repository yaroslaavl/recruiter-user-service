package org.yaroslaavl.userservice.dto.integrations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CompanyExecutedDto(String name,
                                 String nip,
                                 String voivodeship,
                                 String city,
                                 String postCode,
                                 String street) {
}
