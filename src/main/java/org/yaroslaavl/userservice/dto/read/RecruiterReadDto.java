package org.yaroslaavl.userservice.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyRole;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecruiterReadDto extends UserReadDto {

    private UUID companyId;

    private String position;

    private CompanyRole companyRole;
}
