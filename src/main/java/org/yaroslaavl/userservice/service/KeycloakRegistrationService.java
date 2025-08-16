package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.dto.registration.UserRegistrationDto;

public interface KeycloakRegistrationService {

    void registerUser(UserRegistrationDto userRegistrationDto, User user);
}
