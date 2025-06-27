package org.yaroslaavl.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.yaroslaavl.userservice.validation.groups.EditAction;
import org.yaroslaavl.userservice.validation.groups.RecruiterAction;

@Data
public class RecruiterPositionRequest {

    @Pattern(message = "{recruiter.registration.position.pattern}", regexp = "^[A-Za-z ]{1,25}$", groups = {EditAction.class, RecruiterAction.class})
    @NotBlank(message = "{recruiter.registration.position}", groups = {EditAction.class, RecruiterAction.class})
    private String position;
}
