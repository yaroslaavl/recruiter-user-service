package org.yaroslaavl.userservice.mapper;

import org.mapstruct.*;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.CandidateProfileData;
import org.yaroslaavl.userservice.dto.response.*;
import org.yaroslaavl.userservice.dto.request.CandidateInfoRequest;
import org.yaroslaavl.userservice.feignClient.dto.UserFeignDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LanguageMapper.class})
public interface CandidateMapper extends BaseMapper<CandidateResponseDto, Candidate, CandidateInfoRequest> {

    @Override
    CandidateResponseDto toDto(Candidate candidate);

    @Mapping(target = "userId", source = "keycloakId")
    @Mapping(target = "displayName", expression = "java(getCandidateFirstAndLastName(candidate))")
    @Mapping(target = "salary", source = "profileData.desiredSalary")
    @Mapping(target = "workMode", source = "profileData.workMode")
    @Mapping(target = "availableHoursPerWeek", source = "profileData.availableHoursPerWeek")
    @Mapping(target = "availableFrom", source = "profileData.availableFrom")
    UserFeignDto toFeignDto(Candidate candidate);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CandidateInfoRequest candidateInfoRequest, @MappingTarget Candidate candidate);

    @Mapping(target = "candidateId", source = "profileData.candidate.id")
    @Mapping(target = "languageResponseDtoList", source = "profileData.candidate.languages")
    CandidateProfileDataResponseDto toDto(CandidateProfileData profileData);

    @Mapping(target = "linkedinLink", source = "linkedinLink")
    @Mapping(target = "displayName", expression = "java(getCandidateFirstAndLastName(candidate))")
    @Mapping(target = "candidateProfileDataResponseDto", source = "profileData")
    CandidatePrivateResponseDto toPrivateDto(Candidate candidate);

    @Mapping(target = "displayName", expression = "java(getCandidateFirstAndLastName(candidate))")
    @Mapping(target = "candidateProfileDataResponseDto", source = "profileData")
    CandidatePublicResponseDto toPublicDto(Candidate candidate);

    default String getCandidateFirstAndLastName(Candidate candidate) {
        return candidate.getFirstName() + " " + candidate.getLastName();
    }
}
