package org.yaroslaavl.userservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.request.CandidateInfoRequest;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CandidateMapper extends BaseMapper<CandidateReadDto, Candidate, CandidateInfoRequest> {

    @Override
    CandidateReadDto toDto(Candidate candidate);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CandidateInfoRequest candidateInfoRequest, @MappingTarget Candidate candidate);
}
