package com.ja.features.resume.dto;

import java.util.List;

public record ResumeAnalysisDto(
        int score,
        int jobMatchScore,
        int skillScore,
        int projectScore,
        int experienceScore,
        int atsScore,
        int keywordScore,
        List<String> strengths,
        List<String> missingSkills,
        List<String> suggestions,
        List<String> atsKeywords
) {}
