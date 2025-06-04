package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.registration.InitialRegistrationRequestDto;
import org.yaroslaavl.userservice.exception.EmailAlreadyRegisteredException;
import org.yaroslaavl.userservice.exception.EmailVerificationCodeNotEqualException;
import org.yaroslaavl.userservice.exception.EmailVerificationExpiredException;
import org.yaroslaavl.userservice.service.EmailVerificationService;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Value("${mail.send_from}")
    private String mailFrom;

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final RedisServiceImpl redisService;

    private static final String MAIL_SUBJECT = "REGISTRATION CONFIRMATION CODE";
    private static final String VERIFICATION = "VERIFICATION_";

    @Override
    public void requestVerification(InitialRegistrationRequestDto initialRegistrationRequestDto) {
        String email = initialRegistrationRequestDto.getEmail();
        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isEmpty()) {
            String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1_000_000));
            String redisKey = VERIFICATION + email;

            String hasToken = redisService.hasToken(redisKey);
            if (hasToken != null && !hasToken.isEmpty()) {
                redisService.deleteToken(redisKey);
            }

            try {
                sendMailVerificationCode(email, code);
                log.info("Mail sent to: {}", email);
            } catch (MailException e) {
                log.error(e.getMessage());
                throw new MailSendException("Unable to send email to: " + email);
            }

            redisService.setToken(redisKey, code, 10, TimeUnit.MINUTES);
        } else {
            log.error("{} already registered.", email);
            throw new EmailAlreadyRegisteredException("The mail has been registered in the past");
        }
    }

    @Override
    public void verifyCode(String verificationCode, String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        String redisKey = VERIFICATION + email;

        if (userByEmail.isPresent()) {
            log.error("Email {} is already registered in the system", email);
            throw new EmailAlreadyRegisteredException("The email has been registered in the past");
        }

        try {
            String storedCode = redisService.hasToken(redisKey);
            if (storedCode == null || storedCode.isEmpty()) {
                throw new EmailVerificationExpiredException("Email verification session is expired");
            }

            if (!verificationCode.equals(storedCode)) {
                throw new EmailVerificationCodeNotEqualException("Verification code does not match the stored code");
            }

            redisService.setToken(redisKey, "VERIFIED_EMAIL", 10, TimeUnit.MINUTES);
            log.info("Email {} successfully verified", email);
        } catch (Exception e) {
            log.error("Failed to verify code for email {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to verify email due to an internal error", e);
        }
    }

    private void sendMailVerificationCode(String email, String verificationCode) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject(MAIL_SUBJECT);
        message.setText(verificationCode);

        this.javaMailSender.send(message);
    }
}
