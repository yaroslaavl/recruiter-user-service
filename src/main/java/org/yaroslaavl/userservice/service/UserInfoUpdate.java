package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.mapper.BaseMapper;

public abstract class UserInfoUpdate
               <E extends User,
                I,
                O,
                M extends BaseMapper<O, E, I>> {

    protected final SecurityContextService securityContextService;
    protected final UserRepository userRepository;

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
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        mapper.updateEntity(inputDto, user);

        return mapper.toDto(user);
    }
}
