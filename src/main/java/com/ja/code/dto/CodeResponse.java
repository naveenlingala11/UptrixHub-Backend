package com.ja.code.dto;

import lombok.Builder;

@Builder
public class CodeResponse {

    public boolean success;
    public String output;
    public String error;
    public String wrappedCode;
    public long time;

    public static CodeResponse success(String out, String wrapped, long time) {
        return CodeResponse.builder()
                .success(true)
                .output(out)
                .wrappedCode(wrapped)
                .time(time)
                .build();
    }

    public static CodeResponse error(String err) {
        return CodeResponse.builder()
                .success(false)
                .error(err)
                .build();
    }
}
