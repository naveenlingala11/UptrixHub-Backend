package com.ja.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendOtp(String email, String otp) {
        log.info("DEV MODE → OTP for {} is {}", email, otp);
    }
}
