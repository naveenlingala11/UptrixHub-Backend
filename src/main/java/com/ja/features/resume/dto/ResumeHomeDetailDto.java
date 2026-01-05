package com.ja.features.resume.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ResumeHomeDetailDto(
        int score,
        String label,
        LocalDateTime lastUploadedAt,
        List<String> skills,
        List<String> experienceHighlights,
        List<String> tools
) {}
