package org.yaroslaavl.userservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyRole;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecruiterResponseDto extends UserResponseDto {

    private UUID companyId;

    private String position;

    private CompanyRole companyRole;
}
