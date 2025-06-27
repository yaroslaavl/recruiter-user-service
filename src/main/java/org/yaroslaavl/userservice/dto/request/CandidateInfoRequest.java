package org.yaroslaavl.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.yaroslaavl.userservice.validation.LinkedinLink;
import org.yaroslaavl.userservice.validation.PhoneNumber;
import org.yaroslaavl.userservice.validation.groups.CandidateAction;
import org.yaroslaavl.userservice.validation.groups.EditAction;

@Data
public class CandidateInfoRequest {

    @NotBlank(message = "{candidate.registration.phone.number}", groups = {EditAction.class, CandidateAction.class})
    @PhoneNumber(groups = {EditAction.class, CandidateAction.class})
    private String phoneNumber;

    @LinkedinLink(groups = {EditAction.class, CandidateAction.class})
    private String linkedinLink;

}
