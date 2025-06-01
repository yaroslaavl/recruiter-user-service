package org.yaroslaavl.userservice.dto.registration;

import lombok.Data;

@Data
public class CandidateRegistrationDto extends UserRegistrationDto {

    private String phoneNumber;

    private String linkedinLink;
}
