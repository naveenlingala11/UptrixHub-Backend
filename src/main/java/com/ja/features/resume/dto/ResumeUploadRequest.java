package com.ja.features.resume.dto;

public record ResumeUploadRequest(
        String resumeUrl,
        String targetRole
) {}
