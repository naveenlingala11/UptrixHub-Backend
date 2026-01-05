package com.ja.features.resume.dto;

public record ResumeJobMatchRequest(
        String jobDescription,
        String resumeUrl,
        String targetRole
) {}
