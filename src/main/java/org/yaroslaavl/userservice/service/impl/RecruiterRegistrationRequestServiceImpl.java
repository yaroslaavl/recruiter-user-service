package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyRole;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.repository.RecruiterRegistrationRequestRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.request.RecruiterRegistrationRequestUpdateStatus;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.exception.RecruiterAccountAlreadyApprovedException;
import org.yaroslaavl.userservice.exception.RecruiterRequestCreatedException;
import org.yaroslaavl.userservice.service.RecruiterRegistrationRequestService;
import org.yaroslaavl.userservice.service.SecurityContextService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterRegistrationRequestServiceImpl implements RecruiterRegistrationRequestService {

    private final RecruiterRegistrationRequestRepository recruiterRegistrationRequestRepository;
    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;

    /**
     * Creates a recruiter registration request for the given company and recruiter.
     * The method checks if a request already exists for the recruiter and throws an exception if so.
     * Otherwise, it creates and saves a new registration request with a pending status.
     *
     * @param company the company associated with the registration request
     * @param recruiter the recruiter submitting the registration request
     * @return true if the registration request is successfully created
     * @throws RecruiterRequestCreatedException if a registration request already exists for the recruiter
     */
    @Override
    @Transactional
    public Boolean create(Company company, Recruiter recruiter) {
        if (recruiterRegistrationRequestRepository.existsByRecruiterId(recruiter.getId())) {
            throw new RecruiterRequestCreatedException("Recruiter has already sent a request for an account");
        }

        RecruiterRegistrationRequest recruiterRegistrationRequest = RecruiterRegistrationRequest.builder()
                .recruiter(recruiter)
                .company(company)
                .requestStatus(RequestStatus.PENDING)
                .build();

        recruiterRegistrationRequestRepository.save(recruiterRegistrationRequest);
        return true;
    }

    /**
     * Confirms or rejects a recruiter registration request based on the provided request ID and status.
     * Updates the account status of the associated recruiter and may also update the company status if the recruiter is the main recruiter.
     * Saves the request after processing and sets the reviewer details from the current security context.
     *
     * @param registrationRequestId the unique identifier of the registration request to be confirmed or rejected
     * @param requestStatus the desired status for the registration request (APPROVED or REJECTED)
     * @throws EntityNotFoundException if the registration request with the provided ID and a pending status is not found
     * @throws RecruiterAccountAlreadyApprovedException if the associated recruiter's account is already approved
     */
    @Override
    @Transactional
    public void confirmOrRejectRegistrationRequest(UUID registrationRequestId, RequestStatus requestStatus) {
        RecruiterRegistrationRequest request = recruiterRegistrationRequestRepository.findByIdAndRequestStatus(registrationRequestId, RequestStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Registration request not found"));

        if (request.getRecruiter().getAccountStatus() == AccountStatus.APPROVED) {
            throw new RecruiterAccountAlreadyApprovedException("Recruiter account is already approved");
        }

        if (request.getRequestStatus() == RequestStatus.PENDING) {
            boolean isMainRecruiter = request.getRecruiter().getCompanyRole() == CompanyRole.ADMIN_RECRUITER;

            switch (requestStatus) {
                case APPROVED -> {
                    request.setRequestStatus(RequestStatus.APPROVED);
                    request.getRecruiter().setAccountStatus(AccountStatus.APPROVED);
                    if (isMainRecruiter) {
                        request.getCompany().setCompanyStatus(CompanyStatus.APPROVED);
                    }
                }
                case REJECTED -> {
                    request.setRequestStatus(RequestStatus.REJECTED);
                    request.getRecruiter().setAccountStatus(AccountStatus.REJECTED);
                    if (isMainRecruiter) {
                        request.getCompany().setCompanyStatus(CompanyStatus.REJECTED);
                    }
                }
            }
            String managerContext = securityContextService.getSecurityContext();
            User manager = userRepository.findByEmail(managerContext)
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found"));

            request.setReviewedBy(manager);
            request.setReviewedAt(LocalDateTime.now());
            recruiterRegistrationRequestRepository.save(request);
        }
    }
}
