package org.yaroslaavl.userservice.mapper;

import org.mapstruct.*;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RecruiterMapper extends BaseMapper<RecruiterReadDto, Recruiter, RecruiterPositionRequest> {

    @Override
    @Mapping(target = "companyId", source = "company.id")
    RecruiterReadDto toDto(Recruiter recruiter);

    @Override
    @Mapping(target = "companyRole", ignore = true)
    @Mapping(target = "company", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(RecruiterPositionRequest inputDto, @MappingTarget Recruiter entity);
}
