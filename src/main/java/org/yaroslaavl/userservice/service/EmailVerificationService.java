package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.registration.InitialRegistrationRequestDto;

public interface EmailVerificationService {

    void requestVerification(InitialRegistrationRequestDto initialRegistrationRequestDto);

    void verifyCode(String verificationCode, String email);
}
