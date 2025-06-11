package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.CompanyRole;
import org.yaroslaavl.userservice.database.entity.enums.CompanyStatus;
import org.yaroslaavl.userservice.database.entity.enums.UserType;
import org.yaroslaavl.userservice.database.repository.CandidateRepository;
import org.yaroslaavl.userservice.database.repository.CompanyRepository;
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
    private final CompanyRepository companyRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final CompanyServiceImpl companyService;
    private final KeycloakRegistrationServiceImpl registrationService;
    private final EmailVerificationServiceImpl emailVerificationService;
    private final RecruiterRegistrationRequestServiceImpl recruiterRegistrationRequestService;

    private static final String VERIFICATION = "VERIFICATION_";

    @Override
    @Transactional
    public CandidateReadDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto) {
        String email = emailVerificationService.checkEmailVerification(candidateRegistrationDto.getEmail());

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
            candidateRepository.save(candidate);

            redisService.deleteToken(VERIFICATION + email);
            log.info("Candidate with email: {} registered in the system", email);
            return candidateMapper.toDto(candidate);
        } catch (Exception e) {
            log.error("Error occurred during candidate registration", e);
            if (e instanceof KeyCloakUserCreationException) {
                throw new KeyCloakUserCreationException("Failed to create user");
            }
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public RecruiterReadDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto, CompanyExecutedDto companyExecutedDto) {
        String email = emailVerificationService.checkEmailVerification(recruiterRegistrationDto.getEmail());

        Optional<User> candidateByEmail = userRepository.findByEmail(email);
        if (candidateByEmail.isPresent()) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        Company company = companyService.createOrGet(companyExecutedDto);

        Recruiter recruiterBuilder = Recruiter.builder()
                .email(email)
                .firstName(recruiterRegistrationDto.getFirstName())
                .lastName(recruiterRegistrationDto.getLastName())
                .userType(UserType.RECRUITER)
                .accountStatus(AccountStatus.PENDING_APPROVAL)
                .companyRole(company.getCompanyStatus() == CompanyStatus.PENDING ? CompanyRole.ADMIN_RECRUITER : CompanyRole.RECRUITER)
                .position(recruiterRegistrationDto.getPosition())
                .build();

        try {
            recruiterBuilder.setCompany(company);
            Recruiter savedRecruiter = recruiterRepository.saveAndFlush(recruiterBuilder);
            registrationService.registerUser(recruiterRegistrationDto, recruiterBuilder);
            recruiterRegistrationRequestService.create(company, savedRecruiter);

            redisService.deleteToken(VERIFICATION + email);
            log.info("Recruiter with email: {} registered in the system", email);
            return recruiterMapper.toDto(savedRecruiter);
        } catch (Exception e) {
            log.error("Error occurred during recruiter registration", e);
            if (e instanceof KeyCloakUserCreationException) {
                throw new KeyCloakUserCreationException("Failed to create user");
            }
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }
}
