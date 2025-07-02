package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.read.CompanyReadDto;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyExecutedDto toExecutedDto(Company company);

    CompanyReadDto toDto(Company company);
}
