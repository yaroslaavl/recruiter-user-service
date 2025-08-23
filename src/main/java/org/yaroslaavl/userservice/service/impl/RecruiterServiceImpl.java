package org.yaroslaavl.userservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.repository.RecruiterRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.mapper.RecruiterMapper;
import org.yaroslaavl.userservice.service.CompanyService;
import org.yaroslaavl.userservice.service.RecruiterService;
import org.yaroslaavl.userservice.service.SecurityContextService;
import org.yaroslaavl.userservice.service.UserInfoUpdate;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RecruiterServiceImpl extends UserInfoUpdate<Recruiter, RecruiterPositionRequest, RecruiterReadDto, RecruiterMapper> implements RecruiterService {

    private final RecruiterMapper recruiterMapper;
    private final RecruiterRepository recruiterRepository;
    private final CompanyService companyService;

    public RecruiterServiceImpl(SecurityContextService securityContextService,
                                UserRepository userRepository,
                                RecruiterMapper recruiterMapper, RecruiterRepository recruiterRepository, CompanyService companyService) {
        super(securityContextService, userRepository);
        this.recruiterMapper = recruiterMapper;
        this.recruiterRepository = recruiterRepository;
        this.companyService = companyService;
    }

    @Override
    @Transactional
    public RecruiterReadDto updateUserInfo(RecruiterPositionRequest inputDto) {
        return super.updateUserInfo(inputDto, recruiterMapper);
    }

    @Override
    public boolean isRecruiterBelongToCompany(String recruiterKeyId, UUID companyId) {
         Recruiter recruiterByKeycloakId = recruiterRepository.findRecruiterByKeycloakId(recruiterKeyId)
                 .orElseThrow(() -> new EntityNotFoundException("Recruiter not found"));

        Company company = companyService.getCompanyById(companyId);

        return recruiterByKeycloakId.getCompany().getId().equals(company.getId());
    }
}

