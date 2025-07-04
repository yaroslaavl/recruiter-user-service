package org.yaroslaavl.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.yaroslaavl.userservice.validation.groups.EditAction;

@Getter
@RequiredArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "{user.password.request}", groups = EditAction.class)
    private final String currentPassword;

    @Pattern(message = "{user.registration.password.pattern}", regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", groups = EditAction.class)
    @NotBlank(message = "{user.registration.password}", groups = EditAction.class)
    private final String newPassword;
}
