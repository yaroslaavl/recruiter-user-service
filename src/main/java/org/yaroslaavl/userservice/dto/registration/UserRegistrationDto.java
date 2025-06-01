package org.yaroslaavl.userservice.dto.registration;

import lombok.Data;

@Data
public class UserRegistrationDto {

    private String email;

    private String password;

    private String firstName;

    private String lastName;
}
