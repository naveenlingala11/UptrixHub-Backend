package com.ja.auth.dto;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String email;
    private String password;
}
