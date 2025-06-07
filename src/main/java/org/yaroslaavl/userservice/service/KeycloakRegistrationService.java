package org.yaroslaavl.userservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.dto.registration.UserRegistrationDto;

public interface KeycloakRegistrationService {

    void registerUser(UserRegistrationDto userRegistrationDto, User user);

    UserRepresentation getUserRepresentation(UserRegistrationDto userRegistrationDto);
}
