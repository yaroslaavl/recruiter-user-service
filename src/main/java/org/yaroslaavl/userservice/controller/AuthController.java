package org.yaroslaavl.userservice.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RecruiterRegistrationDto;
import org.yaroslaavl.userservice.dto.registration.RegistrationRecruiterAndCompanyDto;
import org.yaroslaavl.userservice.service.AuthService;
import org.yaroslaavl.userservice.service.TokenService;
import org.yaroslaavl.userservice.validation.groups.CandidateAction;
import org.yaroslaavl.userservice.validation.groups.CreateAction;
import org.yaroslaavl.userservice.validation.groups.RecruiterAction;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthService authKeycloakService;
    private final TokenService tokenService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register-candidate")
    public ResponseEntity<CandidateReadDto> registerCandidate(
            @RequestBody @Validated({CreateAction.class, CandidateAction.class}) CandidateRegistrationDto candidateRegistrationDto) {

        CandidateReadDto candidateAccount = authKeycloakService.createCandidateAccount(candidateRegistrationDto);
        return ResponseEntity.ok(candidateAccount);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register-recruiter")
    public ResponseEntity<RecruiterReadDto> registerRecruiter(
            @RequestBody RegistrationRecruiterAndCompanyDto registrationRecruiterAndCompanyDto) {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<RecruiterRegistrationDto>> validate =
                validator.validate(registrationRecruiterAndCompanyDto.getRecruiter(), RecruiterAction.class, CreateAction.class);
        if (validate.isEmpty()) {
            RecruiterReadDto recruiterAccount = authKeycloakService.createRecruiterAccount(
                    registrationRecruiterAndCompanyDto.getRecruiter(),
                    registrationRecruiterAndCompanyDto.getCompany());

            return ResponseEntity.ok(recruiterAccount);
        }

        return ResponseEntity.ok(null);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> loginUser(@RequestBody LoginDto loginDto) {
        AuthTokenDto login = tokenService.login(loginDto);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthTokenDto> refreshToken(@RequestParam("refresh_token") String refreshToken) {
        AuthTokenDto authTokenDto = tokenService.refreshToken(refreshToken);
        return ResponseEntity.ok(authTokenDto);
    }
}
