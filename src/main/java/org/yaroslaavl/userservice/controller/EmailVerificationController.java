package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.dto.registration.InitialRegistrationRequestDto;
import org.yaroslaavl.userservice.service.EmailVerificationService;
import org.yaroslaavl.userservice.validation.groups.CandidateAction;
import org.yaroslaavl.userservice.validation.groups.RecruiterAction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail/")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/request-verification-candidate")
    public void sendRequestVerificationCandidate(@RequestBody @Validated(CandidateAction.class)
                                                     InitialRegistrationRequestDto initialRegistrationRequestDto) {
        emailVerificationService.requestVerification(initialRegistrationRequestDto);
    }

    @PostMapping("/request-verification-recruiter")
    public void sendRequestVerificationRecruiter(@RequestBody @Validated(RecruiterAction.class)
                                                     InitialRegistrationRequestDto initialRegistrationRequestDto) {
        emailVerificationService.requestVerification(initialRegistrationRequestDto);
    }

    @PostMapping("/verify-code")
    public void verifyEmailCode(@RequestParam("verificationCode") String verificationCode,
                                @RequestParam("email") String email) {
        emailVerificationService.verifyCode(verificationCode, email);
    }
}
