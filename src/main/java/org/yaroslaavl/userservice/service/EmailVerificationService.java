package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.registration.InitialRegistrationRequestDto;
import org.yaroslaavl.userservice.dto.registration.UserRegistrationDto;

public interface EmailVerificationService {

    void requestVerification(InitialRegistrationRequestDto initialRegistrationRequestDto);

    void verifyCode(String verificationCode, String email);

    String checkEmailVerification(UserRegistrationDto userRegistrationDto);
}
