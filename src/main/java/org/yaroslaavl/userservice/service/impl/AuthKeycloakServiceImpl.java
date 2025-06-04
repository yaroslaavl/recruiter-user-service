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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.UserType;
import org.yaroslaavl.userservice.database.repository.CandidateRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.UserRegistrationDto;
import org.yaroslaavl.userservice.exception.AuthLoginException;
import org.yaroslaavl.userservice.exception.EmailVerificationExpiredException;
import org.yaroslaavl.userservice.exception.KeyCloakUserCreationException;
import org.yaroslaavl.userservice.mapper.CandidateMapper;
import org.yaroslaavl.userservice.service.AuthService;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthKeycloakServiceImpl implements AuthService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin_client_id}")
    private String clientId;

    @Value("${keycloak.admin_client_secret}")
    private String clientSecret;

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;
    private final RoleServiceImpl roleService;
    private final RedisServiceImpl redisService;
    private final UserRepository userRepository;
    private final CandidateMapper candidateMapper;
    private final CandidateRepository candidateRepository;

    private static final Integer HTTP_CREATE_STATUS = 201;
    private static final String VERIFICATION = "VERIFICATION_";
    private static final String EMAIL_STATUS_VERIFICATION = "VERIFIED_EMAIL";
    private static final String CANDIDATE_REGISTERED_ROLE = "ROLE_VERIFIED_CANDIDATE";
    public static final String KeyCloakAuthTokenUrl = "http://localhost:9090/realms/{0}/protocol/openid-connect/token";

    @Override
    @Transactional
    public CandidateReadDto createCandidateAccount(CandidateRegistrationDto candidateRegistrationDto) {
        String email = candidateRegistrationDto.getEmail();

        String hasToken = redisService.hasToken(VERIFICATION + email);
        if (!hasToken.equals(EMAIL_STATUS_VERIFICATION)) {
            log.warn("Verification code is expired or not valid");
            throw new EmailVerificationExpiredException("Verification code is expired or not valid");
        }

        Candidate candidate = Candidate.builder()
                .email(email)
                .firstName(candidateRegistrationDto.getFirstName())
                .lastName(candidateRegistrationDto.getLastName())
                .userType(UserType.CANDIDATE)
                .accountStatus(AccountStatus.PROFILE_INCOMPLETE)
                .phoneNumber(candidateRegistrationDto.getPhoneNumber())
                .linkedinLink(candidateRegistrationDto.getLinkedinLink())
                .createdAt(LocalDateTime.now())
                .build();

        try {
            createUserRepresentation(candidateRegistrationDto, candidate);
        } catch (KeyCloakUserCreationException kce) {
            throw new KeyCloakUserCreationException("Failed to create user");
        }

        candidateRepository.save(candidate);
        redisService.deleteToken(VERIFICATION + email);
        log.info("User with email: {} registered in the system", email);
        return candidateMapper.toDto(candidate);
    }

    @Override
    public RecruiterReadDto createRecruiterAccount(RecruiterRegistrationDto recruiterRegistrationDto) {
        return null;
    }

    @Override
    public UserRepresentation getUserById(String id) {
        return getUserResource().get(id).toRepresentation();
    }

    @Override
    public AuthTokenDto login(LoginDto loginDto) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", loginDto.email());
        formData.add("password", loginDto.password());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(formData, headers);

        String formattedUrl = MessageFormat.format(KeyCloakAuthTokenUrl, clientId);
        try {
            ResponseEntity<AuthTokenDto> authTokenDto = restTemplate.exchange(formattedUrl,
                    HttpMethod.POST,
                    httpEntity,
                    AuthTokenDto.class);

            if (!authTokenDto.getStatusCode().is2xxSuccessful()) {
                log.error("Response status is not 2xx");
                throw new AuthLoginException("Failed to login");
            }

            log.info("User '{}' authenticated successfully with status: {}", loginDto.email(), authTokenDto.getStatusCode());
            return authTokenDto.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Keycloak error response: {}", ex.getResponseBodyAsString());
            throw new AuthLoginException("Token retrieval error: " + ex.getMessage());
        }
    }

    private void createUserRepresentation(UserRegistrationDto userRegistrationDto, User user) {
        UserRepresentation userRepresentation = getUserRepresentation(userRegistrationDto);
        UsersResource usersResource = getUserResource();
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == HTTP_CREATE_STATUS) {
            String location = response.getHeaderString("Location");
            String keycloakId = location.substring(location.lastIndexOf("/") + 1);

            user.setKeycloakId(keycloakId);
            userRepository.saveAndFlush(user);

            roleService.assignRole(keycloakId, CANDIDATE_REGISTERED_ROLE);
            log.info("User with email {} successfully created in Keycloak", userRegistrationDto.getEmail());
            return;
        }

        String errorMessage = response.readEntity(String.class);
        log.error("Failed to create user with email {} in Keycloak. Status: {}, Error: {}",
                userRegistrationDto.getEmail(), response.getStatus(), errorMessage);

        throw new KeyCloakUserCreationException("Failed to create user with email " + userRegistrationDto.getEmail() +" in Keycloak. Status: {"+response.getStatus()+"}, Error: " + errorMessage);
    }

    private static UserRepresentation getUserRepresentation(UserRegistrationDto userRegistrationDto) {
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
