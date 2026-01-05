package com.ja.home.home.dto;

import java.util.List;

public record SalaryInsightDto(
        int currentCTC,
        int expectedCTC,
        int confidence,
        List<RoleRange> roles,
        List<String> improvements
) {
    public record RoleRange(String role, String range) {}
}
