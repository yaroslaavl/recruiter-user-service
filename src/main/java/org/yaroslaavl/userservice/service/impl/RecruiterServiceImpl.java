package org.yaroslaavl.userservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;
import org.yaroslaavl.userservice.mapper.RecruiterMapper;
import org.yaroslaavl.userservice.service.RecruiterService;
import org.yaroslaavl.userservice.service.SecurityContextService;
import org.yaroslaavl.userservice.service.UserInfoUpdate;

@Service
@Transactional(readOnly = true)
public class RecruiterServiceImpl extends UserInfoUpdate<Recruiter, RecruiterPositionRequest, RecruiterReadDto, RecruiterMapper> implements RecruiterService {

    private final RecruiterMapper recruiterMapper;

    public RecruiterServiceImpl(SecurityContextService securityContextService,
                                UserRepository userRepository,
                                RecruiterMapper recruiterMapper) {
        super(securityContextService, userRepository);
        this.recruiterMapper = recruiterMapper;
    }

    @Override
    @Transactional
    public RecruiterReadDto updateUserInfo(RecruiterPositionRequest inputDto) {
        return super.updateUserInfo(inputDto, recruiterMapper);
    }
}

