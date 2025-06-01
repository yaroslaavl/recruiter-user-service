package org.yaroslaavl.userservice.dto.registration;

import lombok.Data;

@Data
public class RecruiterRegistrationDto extends UserRegistrationDto {

    private String position;
}
