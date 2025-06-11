package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyExecutedDto toDto(Company company);
}
