package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyRole;
import org.yaroslaavl.userservice.database.entity.enums.user.UserType;
import org.yaroslaavl.userservice.database.repository.CandidateRepository;
import org.yaroslaavl.userservice.database.repository.CompanyRepository;
import org.yaroslaavl.userservice.database.repository.RecruiterRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.response.CandidateResponseDto;
import org.yaroslaavl.userservice.dto.response.RecruiterResponseDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;
import org.yaroslaavl.userservice.exception.KeyCloakException;
import org.yaroslaavl.userservice.exception.UserAlreadyRegisteredException;
import org.yaroslaavl.userservice.feignClient.notification.NotificationFeignClient;
import org.yaroslaavl.userservice.mapper.CandidateMapper;
import org.yaroslaavl.userservice.mapper.RecruiterMapper;
import org.yaroslaavl.userservice.service.*;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RecruiterMapper recruiterMapper;
    private final CandidateMapper candidateMapper;
    private final CompanyRepository companyRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final CompanyService companyService;
    private final KeycloakRegistrationService registrationService;
    private final RecruiterRegistrationRequestService recruiterRegistrationRequestService;
    private final NotificationFeignClient notificationFeignClient;

    /**
     * Creates a new candidate account based on the provided registration details.
     *
     * This method verifies the provided email for validation, checks if a user
     * with the given email already exists, and registers a new candidate in the
     * system. It sets the candidate's account status to PROFILE_INCOMPLETE and
     * saves the candidate's information in the repository. If any registration
     * errors occur, an appropriate exception is thrown.
     *
     * @param candidateRegistrationDto the data transfer object containing the
     *                                 candidate's registration details such as
     *                                 email, first name, last name, phone number,
     *                                 and LinkedIn link
     * @return a data transfer object containing the details of the registered
     *         candidate
     * @throws UserAlreadyRegisteredException if a user with the given email is
     *                                        already registered in the system
     * @throws KeyCloakException              if there is a failure during the
     *                                        Keycloak integration process
     * @throws RuntimeException               if there is any other failure during
     *                                        the registration process
     */
    @Override
    @Transactional
    public CandidateResponseDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto) {
        String email = notificationFeignClient.checkEmailVerification(candidateRegistrationDto.getEmail());

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
                .isTemporaryBlocked(Boolean.FALSE)
                .build();

        try {
            registrationService.registerUser(candidateRegistrationDto, candidate);
            candidateRepository.save(candidate);

            log.info("Candidate with email: {} registered in the system", email);
            return candidateMapper.toDto(candidate);
        } catch (Exception e) {
            log.error("Error occurred during candidate registration", e);
            if (e instanceof KeyCloakException) {
                throw new KeyCloakException("Failed to create user");
            }
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new recruiter account based on the provided registration details.
     *
     * This method validates the provided email, checks if a user with the given email
     * already exists, and registers a new recruiter account in the system. It associates
     * the recruiter with the given company, assigns appropriate account status and company
     * roles, and saves the recruiter's information in the database. If any registration
     * error occurs, an appropriate exception is thrown.
     *
     * @param recruiterRegistrationDto the data transfer object containing the recruiter's
     *                                 registration details such as email, first name, last
     *                                 name, and position
     * @param companyExecutedDto       the data transfer object containing the company details
     *                                 required for creating or getting the company during
     *                                 recruiter registration
     * @return a data transfer object containing the details of the registered recruiter
     * @throws UserAlreadyRegisteredException if a user with the given email is already
     *                                        registered in the system
     * @throws KeyCloakException              if there is a failure during the Keycloak
     *                                        integration process
     * @throws RuntimeException               if there is any other failure during the
     *                                        registration process
     */
    @Override
    @Transactional
    public RecruiterResponseDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto, CompanyExecutedDto companyExecutedDto) {
        String email = notificationFeignClient.checkEmailVerification(recruiterRegistrationDto.getEmail());

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
                .companyRole(companyRepository.findCompanyByIdAndRecruiterListIsEmpty(company.getId()).isEmpty() ? CompanyRole.RECRUITER : CompanyRole.ADMIN_RECRUITER)
                .position(recruiterRegistrationDto.getPosition())
                .build();

        try {
            recruiterBuilder.setCompany(company);
            Recruiter savedRecruiter = recruiterRepository.saveAndFlush(recruiterBuilder);
            registrationService.registerUser(recruiterRegistrationDto, recruiterBuilder);
            recruiterRegistrationRequestService.create(company, savedRecruiter);

            log.info("Recruiter with email: {} registered in the system", email);
            return recruiterMapper.toDto(savedRecruiter);
        } catch (Exception e) {
            log.error("Error occurred during recruiter registration", e);
            if (e instanceof KeyCloakException) {
                throw new KeyCloakException("Failed to create user");
            }
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }
}
