package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.dto.registration.InitialRegistrationRequestDto;
import org.yaroslaavl.userservice.service.impl.EmailVerificationServiceImpl;

@RestController
@RequestMapping("/api/v1/mail/")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationServiceImpl emailVerificationService;

    @PostMapping("/request-verification")
    public void sendRequestVerification(@RequestBody InitialRegistrationRequestDto initialRegistrationRequestDto) {
        emailVerificationService.requestVerification(initialRegistrationRequestDto);
    }

    @PostMapping("/verify-code")
    public void verifyEmailCode(@RequestParam("verificationCode") String verificationCode,
                                @RequestParam("email") String email) {
        emailVerificationService.verifyCode(verificationCode, email);
    }
}
