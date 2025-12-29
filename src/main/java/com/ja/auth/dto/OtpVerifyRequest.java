package com.ja.auth.dto;

import lombok.Data;

@Data
public class OtpVerifyRequest {

    // either email OR mobile (one is enough)
    private String email;
    private String mobile;

    private String otp;
}
