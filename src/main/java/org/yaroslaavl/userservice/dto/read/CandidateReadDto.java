package org.yaroslaavl.userservice.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CandidateReadDto extends UserReadDto {

    private String phoneNumber;

    private String linkedinLink;
}
