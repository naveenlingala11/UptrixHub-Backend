package com.ja.home.kit.dto;

public record UserKitDto(
        String kitCode,
        String title,
        int progress,
        boolean active,
        boolean purchased,
        String route
) {}
