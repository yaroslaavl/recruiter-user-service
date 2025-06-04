package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationDto {

    private String email;

    private String password;

    private String firstName;

    private String lastName;
}
