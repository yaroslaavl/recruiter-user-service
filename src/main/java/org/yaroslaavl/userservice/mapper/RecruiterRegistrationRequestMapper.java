package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;
import org.yaroslaavl.userservice.dto.response.RecruiterRegistrationRequestResponseDto;
import org.yaroslaavl.userservice.dto.response.list.RecruiterRegistrationRequestShortDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecruiterRegistrationRequestMapper {

    @Mapping(target = "recruiterEmail", source = "recruiter.email")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "reviewedBy", expression = "java(getReviewerFirstAndLastName(recruiterRegistrationRequest))")
    RecruiterRegistrationRequestShortDto toShortDto(RecruiterRegistrationRequest recruiterRegistrationRequest);

    List<RecruiterRegistrationRequestShortDto> toShortDto(List<RecruiterRegistrationRequest> recruiterRegistrationRequests);

    @Mapping(target = "recruiterId", source = "recruiter.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "companyStatus", source = "company.companyStatus")
    @Mapping(target = "reviewedBy", expression = "java(getReviewerFirstAndLastName(recruiterRegistrationRequest))")
    RecruiterRegistrationRequestResponseDto toDto(RecruiterRegistrationRequest recruiterRegistrationRequest);

    default String getReviewerFirstAndLastName(RecruiterRegistrationRequest recruiterRegistrationRequest) {
        return recruiterRegistrationRequest.getReviewedBy().getFirstName() + " " + recruiterRegistrationRequest.getReviewedBy().getLastName();
    }
}
