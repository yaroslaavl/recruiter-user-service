package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;
import org.yaroslaavl.userservice.database.repository.RecruiterRegistrationRequestRepository;
import org.yaroslaavl.userservice.exception.RecruiterRequestCreatedException;
import org.yaroslaavl.userservice.service.RecruiterRegistrationRequestService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterRegistrationRequestServiceImpl implements RecruiterRegistrationRequestService {

    private final RecruiterRegistrationRequestRepository recruiterRegistrationRequestRepository;

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

}
