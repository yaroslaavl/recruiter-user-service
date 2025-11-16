package org.yaroslaavl.userservice.mapper;

import org.mapstruct.*;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.dto.response.RecruiterPrivateResponseDto;
import org.yaroslaavl.userservice.dto.response.RecruiterPublicResponseDto;
import org.yaroslaavl.userservice.dto.response.RecruiterResponseDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RecruiterMapper extends BaseMapper<RecruiterResponseDto, Recruiter, RecruiterPositionRequest> {

    @Override
    @Mapping(target = "companyId", source = "company.id")
    RecruiterResponseDto toDto(Recruiter recruiter);

    @Override
    @Mapping(target = "companyRole", ignore = true)
    @Mapping(target = "company", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(RecruiterPositionRequest inputDto, @MappingTarget Recruiter entity);

    @Mapping(target = "displayName", expression = "java(getRecruiterFirstAndLastName(recruiter))")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "companyId", source = "company.id")
    RecruiterPrivateResponseDto toPrivateDto(Recruiter recruiter);

    @Mapping(target = "displayName", expression = "java(getRecruiterFirstAndLastName(recruiter))")
    @Mapping(target = "companyName", source = "company.name")
    RecruiterPublicResponseDto toPublicDto(Recruiter recruiter);

    default String getRecruiterFirstAndLastName(Recruiter recruiter) {
        return recruiter.getFirstName() + " " + recruiter.getLastName();
    }
}
