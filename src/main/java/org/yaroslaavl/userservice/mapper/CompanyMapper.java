package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.response.CompanyResponseDto;
import org.yaroslaavl.userservice.dto.response.list.CompanyShortDto;
import org.yaroslaavl.userservice.feignClient.dto.CompanyPreviewFeignDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyExecutedDto toExecutedDto(Company company);

    @Mapping(target = "vacancies", expression = "java(getCompanyVacanciesCount(company, companyVacanciesCount))")
    CompanyResponseDto toDto(Company company, Map<UUID, Long> companyVacanciesCount);

    @Mapping(target = "vacancies", expression = "java(getCompanyVacanciesCount(company, companyVacanciesCount))")
    CompanyShortDto toShortDto(Company company, Map<UUID, Long> companyVacanciesCount);

    default List<CompanyShortDto> toShortDto(List<Company> companyList, Map<UUID, Long> companyVacanciesCount) {
        return companyList.stream()
                .map(company -> toShortDto(company, companyVacanciesCount))
                .toList();
    }

    @Mapping(target = "location", expression = "java(location(company))")
    CompanyPreviewFeignDto toPreviewFeignDto(Company company);

    default String location(Company company) {
        return company.getCity() + ", " +  company.getStreet();
    }

    default Long getCompanyVacanciesCount(Company company, Map<UUID, Long> companyVacanciesCount) {
        return companyVacanciesCount.get(company.getId());
    }
}
