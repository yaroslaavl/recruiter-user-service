package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.UserType;
import org.yaroslaavl.userservice.database.repository.CandidateRepository;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.read.UserReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.UserRegistrationDto;
import org.yaroslaavl.userservice.service.AuthService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthKeycloakServiceImpl implements AuthService {

    private final CandidateRepository candidateRepository;

    @Override
    public CandidateReadDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto) {
        Candidate candidate = Candidate.builder()
                .email(candidateRegistrationDto.getEmail())
                .firstName(candidateRegistrationDto.getFirstName())
                .lastName(candidateRegistrationDto.getLastName())
                .userType(UserType.CANDIDATE)
                .phoneNumber(candidateRegistrationDto.getPhoneNumber())
                .linkedinLink(candidateRegistrationDto.getLinkedinLink())
                .build();

        candidateRepository.saveAndFlush(candidate);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(candidateRegistrationDto.getPassword());
        credentialRepresentation.setTemporary(Boolean.FALSE);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        return null;
    }

    @Override
    public RecruiterReadDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto) {
        return null;
    }

    @Override
    public UserReadDto getUserById(UUID id) {
        return null;
    }

    private UserRepresentation createUserRepresentation(UserRegistrationDto userRegistrationDto, User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(Boolean.TRUE);
        userRepresentation.setId(user.getId().toString());
        userRepresentation.setEmail(userRegistrationDto.getEmail());
        userRepresentation.setFirstName(userRegistrationDto.getFirstName());
        userRepresentation.setLastName(userRegistrationDto.getLastName());
        return userRepresentation;
    }
}
