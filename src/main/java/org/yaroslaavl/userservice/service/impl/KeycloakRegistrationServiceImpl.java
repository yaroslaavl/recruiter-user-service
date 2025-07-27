package org.yaroslaavl.userservice.service.impl;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.user.UserType;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.registration.UserRegistrationDto;
import org.yaroslaavl.userservice.exception.KeyCloakException;
import org.yaroslaavl.userservice.service.KeycloakRegistrationService;
import org.yaroslaavl.userservice.service.RoleService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakRegistrationServiceImpl implements KeycloakRegistrationService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;
    private final RoleService roleService;
    private final UserRepository userRepository;

    private static final Integer HTTP_CREATE_STATUS = 201;

    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto, User user) {
        UserRepresentation userRepresentation = getUserRepresentation(userRegistrationDto);
        UsersResource usersResource = getUserResource();
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == HTTP_CREATE_STATUS) {
            String location = response.getHeaderString("Location");
            String keycloakId = location.substring(location.lastIndexOf("/") + 1);

            user.setKeycloakId(keycloakId);
            userRepository.saveAndFlush(user);

            roleService.assignRoles(keycloakId, user.getUserType().equals(UserType.CANDIDATE) ? UserType.CANDIDATE: UserType.RECRUITER);
            log.info("User with email {} successfully created in Keycloak", userRegistrationDto.getEmail());
            return;
        }

        String errorMessage = response.readEntity(String.class);
        log.error("Failed to create user with email {} in Keycloak. Status: {}, Error: {}",
                userRegistrationDto.getEmail(), response.getStatus(), errorMessage);

        throw new KeyCloakException("Failed to create user with email " + userRegistrationDto.getEmail() + " in Keycloak. Status: {" + response.getStatus() + "}, Error: " + errorMessage);
    }

    @Override
    public UserRepresentation getUserRepresentation(UserRegistrationDto userRegistrationDto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(Boolean.TRUE);
        userRepresentation.setEmail(userRegistrationDto.getEmail());
        userRepresentation.setFirstName(userRegistrationDto.getFirstName());
        userRepresentation.setLastName(userRegistrationDto.getLastName());
        userRepresentation.setEmailVerified(Boolean.TRUE);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationDto.getPassword());
        credentialRepresentation.setTemporary(Boolean.FALSE);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> credentialRepresentationList = new ArrayList<>();
        credentialRepresentationList.add(credentialRepresentation);
        userRepresentation.setCredentials(credentialRepresentationList);
        return userRepresentation;

    }

    private UsersResource getUserResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }
}
