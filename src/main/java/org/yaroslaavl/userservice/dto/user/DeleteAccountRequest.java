package org.yaroslaavl.userservice.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.yaroslaavl.userservice.validation.groups.EditAction;

@Data
public class DeleteAccountRequest  {

    @NotBlank(message = "{user.password.request}", groups = EditAction.class)
    private String currentPassword;
}
