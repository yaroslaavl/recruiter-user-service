package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.UserType;
import org.yaroslaavl.userservice.database.repository.CandidateRepository;
import org.yaroslaavl.userservice.database.repository.RecruiterRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;
import org.yaroslaavl.userservice.exception.KeyCloakUserCreationException;
import org.yaroslaavl.userservice.exception.UserAlreadyRegisteredException;
import org.yaroslaavl.userservice.mapper.CandidateMapper;
import org.yaroslaavl.userservice.mapper.RecruiterMapper;
import org.yaroslaavl.userservice.service.AuthService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final RedisServiceImpl redisService;
    private final UserRepository userRepository;
    private final RecruiterMapper recruiterMapper;
    private final CandidateMapper candidateMapper;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final KeycloakRegistrationServiceImpl registrationService;
    private final EmailVerificationServiceImpl emailVerificationService;

    private static final String VERIFICATION = "VERIFICATION_";

    @Override
    @Transactional
    public CandidateReadDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto) {
        String email = emailVerificationService.checkEmailVerification(candidateRegistrationDto);

        Optional<User> candidateByEmail = userRepository.findByEmail(email);
        if (candidateByEmail.isPresent()) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        Candidate candidate = Candidate.builder()
                .email(email)
                .firstName(candidateRegistrationDto.getFirstName())
                .lastName(candidateRegistrationDto.getLastName())
                .userType(UserType.CANDIDATE)
                .accountStatus(AccountStatus.PROFILE_INCOMPLETE)
                .phoneNumber(candidateRegistrationDto.getPhoneNumber())
                .linkedinLink(candidateRegistrationDto.getLinkedinLink())
                .build();

        try {
            registrationService.registerUser(candidateRegistrationDto, candidate);
        } catch (KeyCloakUserCreationException kce) {
            throw new KeyCloakUserCreationException("Failed to create user");
        }

        candidateRepository.save(candidate);
        redisService.deleteToken(VERIFICATION + email);

        log.info("Candidate with email: {} registered in the system", email);
        return candidateMapper.toDto(candidate);
    }

    //Создать таблицу в бд где будет и рекрутер и компания с их статусами и чтобы админ вручную проверял данные.
    @Override
    public RecruiterReadDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto, CompanyExecutedDto companyExecutedDto) {
        String email = emailVerificationService.checkEmailVerification(recruiterRegistrationDto);

        Optional<User> candidateByEmail = userRepository.findByEmail(email);
        if (candidateByEmail.isPresent()) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        Recruiter recruiter = Recruiter.builder()
                .email(email)
                .firstName(recruiterRegistrationDto.getFirstName())
                .lastName(recruiterRegistrationDto.getLastName())
                .userType(UserType.RECRUITER)
                .accountStatus(AccountStatus.PENDING_APPROVAL)
                .position(recruiterRegistrationDto.getPosition())
                .build();

        try {
            registrationService.registerUser(recruiterRegistrationDto, recruiter);
        } catch (KeyCloakUserCreationException kce) {
            throw new KeyCloakUserCreationException("Failed to create user");
        }

        recruiterRepository.save(recruiter);
        redisService.deleteToken(VERIFICATION + email);

        log.info("Recruiter with email: {} registered in the system", email);
        return recruiterMapper.toDto(recruiter);
    }
}
