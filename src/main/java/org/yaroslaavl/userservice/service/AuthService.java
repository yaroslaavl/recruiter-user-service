package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.response.CandidateResponseDto;
import org.yaroslaavl.userservice.dto.response.RecruiterResponseDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;

public interface AuthService {

    CandidateResponseDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto);

    RecruiterResponseDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto, CompanyExecutedDto companyExecutedDto);
}
