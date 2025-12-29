package com.ja.code.dto;

import lombok.Data;
import java.util.List;

@Data
public class CodeRequest {

    private String code;
    private String input;
    private String javaVersion;

    // For judge mode
    private List<TestCase> tests;
}
