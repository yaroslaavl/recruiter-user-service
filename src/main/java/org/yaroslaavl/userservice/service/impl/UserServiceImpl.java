package org.yaroslaavl.userservice.service.impl;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.user.ChangePasswordRequest;
import org.yaroslaavl.userservice.dto.user.DeleteAccountRequest;
import org.yaroslaavl.userservice.exception.KeyCloakUserDeletionException;
import org.yaroslaavl.userservice.exception.UserNotFoundException;
import org.yaroslaavl.userservice.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    private final TokenServiceImpl tokenService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteAccount(DeleteAccountRequest userDeleteDto) {
        String securityContextEmail = getSecurityContextEmail();
        User user = userRepository.findByEmail(securityContextEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + securityContextEmail));

        AuthTokenDto login = tokenService.login(new LoginDto(securityContextEmail, userDeleteDto.getCurrentPassword()));
        String accessToken = login.accessToken();

        try {
            if (accessToken != null && !login.accessToken().isEmpty()) {
                Response delete = keycloak.realm(realm).users().delete(user.getKeycloakId());

                if (delete.getStatus() == 204) {
                    userRepository.delete(user);
                }
            }
        } catch (WebApplicationException we) {
            int status = we.getResponse().getStatus();
            String body = we.getResponse().readEntity(String.class);
            log.error("Failed to delete user. Status: {}, Body: {}", status, body);
            throw new KeyCloakUserDeletionException("Failed to delete user");
        }
    }

    @Override
    @Transactional
    public boolean updatePassword(ChangePasswordRequest resetPasswordDto) {
        return false;
    }

    private String getSecurityContextEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwt) {
            return jwt.getToken().getClaimAsString("email");
        }
        throw new RuntimeException("Email not found in JWT");
    }
}
