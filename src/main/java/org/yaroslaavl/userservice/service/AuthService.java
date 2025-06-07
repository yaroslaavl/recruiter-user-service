package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;

public interface AuthService {

    CandidateReadDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto);

    RecruiterReadDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto, CompanyExecutedDto companyExecutedDto);
}
