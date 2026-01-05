package com.ja.features.resume.dto;

public record ResumeCompareDto(
        int oldScore,
        int newScore,
        int improvement
) {}
