package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RecruiterMapper {

    @Mapping(target = "companyId", ignore = true)
    RecruiterReadDto toDto(Recruiter recruiter);
}
