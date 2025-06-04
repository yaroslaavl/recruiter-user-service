package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CandidateMapper {

    CandidateReadDto toDto(Candidate candidate);
}
