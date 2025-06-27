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

    protected SecurityContextService securityContextService;
    protected UserRepository userRepository;

    protected UserInfoUpdate(SecurityContextService securityContextService, UserRepository userRepository) {
        this.securityContextService = securityContextService;
        this.userRepository = userRepository;
    }

    public O updateUserInfo(I inputDto, M mapper) {
        if (inputDto == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        String userEmail = securityContextService.getSecurityContext();

        E user = (E) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        mapper.updateEntity(inputDto, user);

        return mapper.toDto(user);
    }
}
