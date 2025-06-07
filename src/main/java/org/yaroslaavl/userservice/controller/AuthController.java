package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.registration.CandidateRegistrationDto;
import org.yaroslaavl.userservice.service.impl.AuthServiceImpl;
import org.yaroslaavl.userservice.service.impl.TokenServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthServiceImpl authKeycloakService;
    private final TokenServiceImpl tokenService;

    @PostMapping("/register-candidate")
    public ResponseEntity<CandidateReadDto> registerCandidate(@RequestBody CandidateRegistrationDto candidateRegistrationDto) {
        CandidateReadDto candidateAccount = authKeycloakService.createCandidateAccount(candidateRegistrationDto);
        return ResponseEntity.ok(candidateAccount);
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
