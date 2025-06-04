package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateRegistrationDto extends UserRegistrationDto {

    private String phoneNumber;

    private String linkedinLink;
}
