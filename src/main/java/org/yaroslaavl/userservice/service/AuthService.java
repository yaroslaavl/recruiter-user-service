package org.yaroslaavl.userservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;

public interface AuthService {

    CandidateReadDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto);

    RecruiterReadDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto);

    UserRepresentation getUserById(String id);

    AuthTokenDto login(LoginDto loginDto);
}
