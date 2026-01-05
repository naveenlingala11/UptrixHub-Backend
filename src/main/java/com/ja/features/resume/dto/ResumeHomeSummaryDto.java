package com.ja.features.resume.dto;

import java.util.List;

public record ResumeHomeSummaryDto(
        int score,
        String label,
        List<String> detectedSkills,
        List<String> missingSkills
) {}
