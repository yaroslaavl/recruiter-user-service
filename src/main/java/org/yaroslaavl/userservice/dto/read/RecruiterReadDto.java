package org.yaroslaavl.userservice.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecruiterReadDto extends UserReadDto {

    private UUID companyId;

    private String position;
}
