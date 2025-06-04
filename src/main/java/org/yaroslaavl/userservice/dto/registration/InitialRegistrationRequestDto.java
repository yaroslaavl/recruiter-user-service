package org.yaroslaavl.userservice.dto.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitialRegistrationRequestDto {

    private String email;
}
