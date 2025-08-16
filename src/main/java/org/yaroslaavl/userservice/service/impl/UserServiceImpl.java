package org.yaroslaavl.userservice.service.impl;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.*;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.repository.*;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.request.*;
import org.yaroslaavl.userservice.exception.KeyCloakException;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.service.SecurityContextService;
import org.yaroslaavl.userservice.service.TokenService;
import org.yaroslaavl.userservice.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    private final SecurityContextService securityContextService;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteAccount(DeleteAccountRequest userDeleteDto) {
        UserActionDto userActionDto = changeUserData(userDeleteDto.getCurrentPassword());

        try {
            if (userActionDto.getToken() != null && !userActionDto.getToken().isEmpty()) {
                Response delete = keycloak.realm(realm).users().delete(userActionDto.getUser().getKeycloakId());

                if (delete.getStatus() == 204) {
                    userRepository.delete(userActionDto.getUser());
                }
            }
        } catch (WebApplicationException we) {
            int status = we.getResponse().getStatus();
            String body = we.getResponse().readEntity(String.class);
            log.error("Failed to delete user. Status: {}, Body: {}", status, body);
            throw new KeyCloakException("Failed to delete user");
        }
    }

    @Override
    @Transactional
    public boolean updatePassword(ChangePasswordRequest updatePasswordDto) {
        UserActionDto userActionDto = changeUserData(updatePasswordDto.getCurrentPassword());
        try {
            if (userActionDto.getToken() != null && !userActionDto.getToken().isEmpty()) {
                CredentialRepresentation password = new CredentialRepresentation();
                password.setTemporary(Boolean.FALSE);
                password.setType(CredentialRepresentation.PASSWORD);
                password.setValue(updatePasswordDto.getNewPassword());

                keycloak.realm(realm).users().get(userActionDto.getUser().getKeycloakId()).resetPassword(password);

                AuthTokenDto login =
                        tokenService.login(new LoginDto(userActionDto.getUser().getEmail(), updatePasswordDto.getNewPassword()));

                if (login.accessToken() != null && !login.accessToken().isEmpty()) {
                    return true;
                }
            }
        } catch (WebApplicationException we) {
            int status = we.getResponse().getStatus();
            String body = we.getResponse().readEntity(String.class);
            log.error("Failed to update user. Status: {}, Body: {}", status, body);
            throw new KeyCloakException("Failed to update user");
        }

        return false;
    }

    @Override
    public boolean existsAccount(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isAccountApproved(String userId) {
        User user = userRepository.findByKeycloakId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        return user.getAccountStatus() == AccountStatus.PROFILE_COMPLETE;
    }

    private UserActionDto changeUserData(String password) {
        String securityContextEmail = securityContextService.getSecurityContext();
        User user = userRepository.findByEmail(securityContextEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + securityContextEmail));
        AuthTokenDto login = tokenService.login(new LoginDto(securityContextEmail, password));

        return new UserActionDto(login.accessToken(), user);
    }
}
