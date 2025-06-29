package org.yaroslaavl.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.yaroslaavl.userservice.validation.groups.EditAction;

@Getter
@Setter
public class DeleteAccountRequest  {

    @NotBlank(message = "{user.password.request}", groups = EditAction.class)
    private String currentPassword;
}
