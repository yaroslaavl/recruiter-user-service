package org.yaroslaavl.userservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;
import org.yaroslaavl.userservice.mapper.RecruiterMapper;
import org.yaroslaavl.userservice.service.UserInfoUpdate;

@Service
public class RecruiterServiceImpl extends UserInfoUpdate<Recruiter, RecruiterPositionRequest, RecruiterReadDto, RecruiterMapper> {

    protected RecruiterServiceImpl(SecurityContextServiceImpl securityContextService, UserRepository userRepository, RecruiterMapper mapper) {
        super(securityContextService, userRepository, mapper);
    }

    @Override
    @Transactional
    public RecruiterReadDto updateUserInfo(RecruiterPositionRequest inputDto) {
        return super.updateUserInfo(inputDto);
    }
}

