package org.yaroslaavl.userservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.user.UserType;
import org.yaroslaavl.userservice.database.repository.RecruiterRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.response.*;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;
import org.yaroslaavl.userservice.exception.AccessInfoDeniedException;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.mapper.RecruiterMapper;
import org.yaroslaavl.userservice.service.CompanyService;
import org.yaroslaavl.userservice.service.RecruiterService;
import org.yaroslaavl.userservice.service.SecurityContextService;
import org.yaroslaavl.userservice.service.UserInfoUpdate;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RecruiterServiceImpl extends UserInfoUpdate<Recruiter, RecruiterPositionRequest, RecruiterResponseDto, RecruiterMapper> implements RecruiterService {

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
    public RecruiterResponseDto updateUserInfo(RecruiterPositionRequest inputDto) {
        return super.updateUserInfo(inputDto, recruiterMapper);
    }

    @Override
    public boolean isRecruiterBelongToCompany(String recruiterKeyId, UUID companyId) {
         Recruiter recruiterByKeycloakId = recruiterRepository.findRecruiterByKeycloakId(recruiterKeyId)
                 .orElseThrow(() -> new EntityNotFoundException("Recruiter not found"));

        Company company = companyService.getCompanyById(companyId);

        return recruiterByKeycloakId.getCompany().getId().equals(company.getId());
    }

    @Override
    public RecruiterPrivateResponseDto getPersonalData() {
        String email = securityContextService.getSecurityContext();

        if (email != null && !email.isEmpty()) {
            Recruiter recruiter = recruiterRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Recruiter not found"));

            return recruiterMapper.toPrivateDto(recruiter);
        }

        return null;
    }

    @Override
    public RecruiterPublicResponseDto getPublicData(String recruiterKeyId) {
        String email = securityContextService.getSecurityContext();

        if (email != null && !email.isEmpty()) {
            Recruiter recruiter = recruiterRepository.findByKeycloakId(recruiterKeyId);

            boolean isMe = Objects.equals(recruiterKeyId, recruiter.getKeycloakId());

            if (isMe) {
                return recruiterMapper.toPublicDto(recruiter);
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            if (user.getAccountStatus() == AccountStatus.REJECTED || (user.getUserType() != UserType.CANDIDATE && user.getUserType() != UserType.RECRUITER)) {
                throw new AccessInfoDeniedException("You don't have access to this information");
            }

            return recruiterMapper.toPublicDto(recruiter);
        }

        return null;
    }
}

