package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yaroslaavl.userservice.validation.LinkedinLink;
import org.yaroslaavl.userservice.validation.PhoneNumber;
import org.yaroslaavl.userservice.validation.groups.CandidateAction;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateRegistrationDto extends UserRegistrationDto {

    @NotBlank(message = "{candidate.registration.phone.number}", groups = CandidateAction.class)
    @PhoneNumber(groups = CandidateAction.class)
    private String phoneNumber;

    @LinkedinLink(groups = CandidateAction.class)
    private String linkedinLink;
}
