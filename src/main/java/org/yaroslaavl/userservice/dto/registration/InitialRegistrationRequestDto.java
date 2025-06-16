package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.yaroslaavl.userservice.validation.EmailCandidate;
import org.yaroslaavl.userservice.validation.EmailRecruiter;
import org.yaroslaavl.userservice.validation.groups.CandidateAction;
import org.yaroslaavl.userservice.validation.groups.RecruiterAction;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitialRegistrationRequestDto {

    @EmailCandidate(groups = CandidateAction.class)
    @EmailRecruiter(groups = RecruiterAction.class)
    private String email;
}
