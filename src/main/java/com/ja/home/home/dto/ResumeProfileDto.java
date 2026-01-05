package com.ja.home.home.dto;

import java.util.List;

public record ResumeProfileDto(
        boolean uploaded,
        Integer score,
        List<String> detectedSkills,
        List<String> missingSkills,
        String lastUpdated
) {}
