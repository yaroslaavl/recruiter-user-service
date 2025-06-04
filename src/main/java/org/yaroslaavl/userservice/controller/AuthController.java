package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.service.impl.AuthKeycloakServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthKeycloakServiceImpl authKeycloakService;

    @PostMapping("/register-candidate")
    public ResponseEntity<CandidateReadDto> registerCandidate(@RequestBody CandidateRegistrationDto candidateRegistrationDto) {
        CandidateReadDto candidateAccount = authKeycloakService.createCandidateAccount(candidateRegistrationDto);
        return ResponseEntity.ok(candidateAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> loginUser(@RequestBody LoginDto loginDto) {
        AuthTokenDto login = authKeycloakService.login(loginDto);
        return ResponseEntity.ok(login);
    }
}
