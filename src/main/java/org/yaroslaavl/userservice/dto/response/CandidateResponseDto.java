package org.yaroslaavl.userservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CandidateResponseDto extends UserResponseDto {

    private String phoneNumber;

    private String linkedinLink;
}
