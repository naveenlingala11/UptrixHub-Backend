package com.ja.features.resume.dto;

import java.util.List;

public record ResumeJobMatchDto(
        int matchScore,
        int skillMatchScore,
        int experienceMatchScore,
        int techStackMatchScore,
        int atsMatchScore,
        List<String> matchedSkills,
        List<String> missingSkills,
        List<String> suggestions,
        List<String> mockInterviewTopics
) {}
