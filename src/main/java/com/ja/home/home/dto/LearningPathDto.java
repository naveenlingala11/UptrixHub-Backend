package com.ja.home.home.dto;

public record LearningPathDto(
        String title,
        String description,
        int progress,
        String action,
        String route,
        String status
) {}
