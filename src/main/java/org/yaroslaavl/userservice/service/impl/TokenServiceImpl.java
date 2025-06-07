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
import org.yaroslaavl.userservice.database.entity.enums.AccountStatus;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.exception.AuthLoginException;
import org.yaroslaavl.userservice.exception.UserAlreadyRegisteredException;
import org.yaroslaavl.userservice.exception.UserNotFoundException;
import org.yaroslaavl.userservice.exception.UserVerificationNotAcceptedException;
import org.yaroslaavl.userservice.service.TokenService;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    @Value("${keycloak.admin_client_id}")
    private String clientId;

    @Value("${keycloak.admin_client_secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private static final String KeyCloakAuthTokenUrl = "http://localhost:9090/realms/{0}/protocol/openid-connect/token";

    @Override
    public AuthTokenDto login(LoginDto loginDto) {
        User userByEmail = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new UserNotFoundException("User does not have an account"));

        if (userByEmail instanceof Recruiter recruiter) {
            if (recruiter.getAccountStatus() == AccountStatus.PENDING_APPROVAL) {
                throw new UserVerificationNotAcceptedException("Company is not yet verified");
            }
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("username", loginDto.email());
        formData.add("password", loginDto.password());

        return requestToken(formData, "Login for user " + loginDto.email());
    }

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
