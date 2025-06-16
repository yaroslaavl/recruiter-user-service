package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.yaroslaavl.userservice.validation.groups.CreateAction;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationDto {

    private String email;

    @Pattern(message = "{user.registration.password.pattern}", regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", groups = CreateAction.class)
    @NotBlank(message = "{user.registration.password}", groups = CreateAction.class)
    private String password;

    @Pattern(message = "{user.registration.firstname.pattern}", regexp = "[A-Z][a-zA-Z]*([ '-][A-Z][a-zA-Z]*)*", groups = CreateAction.class)
    @NotBlank(message = "{user.registration.firstname}", groups = CreateAction.class)
    private String firstName;

    @Pattern(message = "{user.registration.lastname.pattern}", regexp = "[A-Z][a-zA-Z]*([ '-][A-Z][a-zA-Z]*)*", groups = CreateAction.class)
    @NotBlank(message = "{user.registration.lastname}", groups = CreateAction.class)
    private String lastName;
}
