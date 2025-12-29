package com.ja.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {
    public void sendOtp(String mobile, String otp) {
        log.info("Sending OTP {} to mobile {}", otp, mobile);
    }
}
