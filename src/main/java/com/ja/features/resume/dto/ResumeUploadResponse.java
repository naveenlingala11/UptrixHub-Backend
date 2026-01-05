package com.ja.features.resume.dto;

public record ResumeUploadResponse(
        String fileName,
        int length,
        String preview,
        String extractedText
) {}
