package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.exception.UserNotFoundException;
import org.yaroslaavl.userservice.mapper.BaseMapper;
import org.yaroslaavl.userservice.service.impl.SecurityContextServiceImpl;

public abstract class UserInfoUpdate
               <E extends User,
                I,
                O,
                M extends BaseMapper<O, E, I>> {

    protected SecurityContextServiceImpl securityContextService;
    protected UserRepository userRepository;
    protected M mapper;

    protected UserInfoUpdate(SecurityContextServiceImpl securityContextService, UserRepository userRepository, M mapper) {
        this.securityContextService = securityContextService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public O updateUserInfo(I inputDto) {
        String userEmail = securityContextService.getSecurityContext();

        E user = (E) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (inputDto == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        mapper.updateEntity(inputDto, user);

        return mapper.toDto(user);
    }
}
