package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yaroslaavl.userservice.validation.groups.RecruiterAction;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecruiterRegistrationDto extends UserRegistrationDto {

    @Pattern(message = "{recruiter.registration.position.pattern}", regexp = "^[A-Za-z ]{1,25}$", groups = RecruiterAction.class)
    @NotBlank(message = "{recruiter.registration.position}", groups = RecruiterAction.class)
    private String position;
}
