package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.read.UserReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;

import java.util.UUID;

public interface AuthService {

    CandidateReadDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto);

    RecruiterReadDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto);

    UserReadDto getUserById(UUID id);
}
