package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecruiterRegistrationDto extends UserRegistrationDto {

    private String position;
}
