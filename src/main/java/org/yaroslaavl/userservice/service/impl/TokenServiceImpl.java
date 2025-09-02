package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.exception.AuthLoginException;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.exception.UserTemporaryBlockedException;
import org.yaroslaavl.userservice.exception.UserVerificationNotAcceptedException;
import org.yaroslaavl.userservice.service.TokenService;

import java.text.MessageFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${keycloak.main_app.client_id}")
    private String clientId;

    @Value("${keycloak.main_app.client_secret}")
    private String clientSecret;

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private static final String KeyCloakAuthTokenUrl = "http://localhost:9090/realms/{0}/protocol/openid-connect/token";

    /**
     * Authenticates a user with the provided login credentials. If the user exists and is eligible,
     * an authentication token is generated and returned.
     * The method performs several validations, such as checking if the user is temporarily blocked,
     * or if the user's account is pending approval (for recruiters).
     *
     * @param loginDto an object containing the user's email and password used for authentication
     * @return an AuthTokenDto object containing the authentication token details
     * @throws EntityNotFoundException if no user with the provided email is found
     * @throws UserTemporaryBlockedException if the user is temporarily blocked
     * @throws UserVerificationNotAcceptedException if the user's account status is pending approval
     * @throws AuthLoginException if there are issues during the authentication process
     */
    @Override
    public AuthTokenDto login(LoginDto loginDto) {
        User userByEmail = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new EntityNotFoundException("User does not have an account"));

        if (userByEmail.getIsTemporaryBlocked() == Boolean.TRUE) {
            throw new UserTemporaryBlockedException("User is temporary blocked");
        }

        if (userByEmail instanceof Recruiter recruiter) {
            if (recruiter.getAccountStatus() == AccountStatus.PENDING_APPROVAL) {
                throw new UserVerificationNotAcceptedException("Not yet verified");
            }
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("username", loginDto.email());
        formData.add("password", loginDto.password());

        return requestToken(formData, "Login for user " + loginDto.email());
    }

    /**
     * Refreshes the authentication token using the provided refresh token.
     * This method submits the refresh token to the token service endpoint to obtain a new access token.
     *
     * @param refreshToken the refresh token to be used for generating a new access token
     * @return an AuthTokenDto object containing the new access token and its associated details
     */
    @Override
    public AuthTokenDto refreshToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);

        return requestToken(formData, "Refresh token");
    }

    private AuthTokenDto requestToken(MultiValueMap<String, String> formData, String operation) {
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(formData, headers);

        String url = MessageFormat.format(KeyCloakAuthTokenUrl, clientId);

        try {
            ResponseEntity<AuthTokenDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    AuthTokenDto.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("{} failed: HTTP {}", operation, response.getStatusCode());
                throw new AuthLoginException("Authentication failed");
            }

            log.info("{} successful with status: {}", operation, response.getStatusCode());
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Keycloak error during {}: {}", operation, ex.getResponseBodyAsString());
            throw new AuthLoginException("Token retrieval error: " + ex.getMessage());
        }
    }
}
