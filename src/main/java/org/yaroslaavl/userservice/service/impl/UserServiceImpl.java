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
import org.yaroslaavl.userservice.database.entity.enums.user.UserType;
import org.yaroslaavl.userservice.database.repository.*;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.request.*;
import org.yaroslaavl.userservice.dto.response.CurrentUser;
import org.yaroslaavl.userservice.exception.AccessInfoDeniedException;
import org.yaroslaavl.userservice.exception.KeyCloakException;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.exception.UserAccountStatusException;
import org.yaroslaavl.userservice.mapper.UserMapper;
import org.yaroslaavl.userservice.service.SecurityContextService;
import org.yaroslaavl.userservice.service.TokenService;
import org.yaroslaavl.userservice.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Value("${keycloak.main_app.realm}")
    private String realm;

    private final Keycloak keycloak;

    private final SecurityContextService securityContextService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Deletes a user account from the system and associated Keycloak identity provider.
     * Validates the current user credentials and ensures successful deletion in both the application
     * and Keycloak system.
     *
     * @param userDeleteDto the request object containing the current password of the user for authentication
     *        and deletion authorization
     * @throws KeyCloakException if the Keycloak user deletion fails due to an external system error
     */
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

    /**
     * Updates the password for the currently authenticated user. The method validates the current password,
     * updates the user's password in Keycloak, and verifies the change by attempting to re-authenticate the user
     * with the new password.
     *
     * @param updatePasswordDto the request object containing the current password and the new password for the update
     * @return true if the password update and re-authentication were successful; false otherwise
     * @throws KeyCloakException if there is an error during interaction with Keycloak
     */
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
        log.info("Checking if user with id: {} is approved", userId);
        User user = userRepository.findByKeycloakId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (user instanceof Candidate candidate) {
            return candidate.getAccountStatus() == AccountStatus.PROFILE_COMPLETE;
        }

        if (user instanceof Recruiter recruiter) {
            return recruiter.getAccountStatus() == AccountStatus.APPROVED;
        }

        return false;
    }

    @Override
    public Map<String, String> usersDisplayName(Set<String> userIds, String currentUserEmail) {
        User user = checkUserData(currentUserEmail);

        if (user.getAccountStatus().equals(AccountStatus.REJECTED)) {
            throw new UserAccountStatusException("Account status is rejected");
        }

        if (user.getUserType() != UserType.MANAGER) {
            throw new AccessInfoDeniedException("You don't have access to this information");
        }

        List<User> users = userRepository.findUsersByUserKeycloakId(userIds);

        return users.stream().collect(Collectors.toMap(User::getKeycloakId, u -> u.getFirstName() + " " + u.getLastName()));
    }

    @Override
    public CurrentUser getCurrentUser() {
        User user = userRepository.findByEmail(securityContextService.getSecurityContext())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + securityContextService.getSecurityContext()));
        return userMapper.toCurrentUser(user);
    }

    private UserActionDto changeUserData(String password) {
        User user = checkUserData(null);
        AuthTokenDto login = tokenService.login(new LoginDto(user.getEmail(), password));

        return new UserActionDto(login.accessToken(), user);
    }

    private User checkUserData(String currentUserEmail) {
        String email;
        if (currentUserEmail != null) {
            email = currentUserEmail;
        } else {
            email = securityContextService.getSecurityContext();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with notification: " + email));
    }
}
