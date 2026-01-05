package com.ja.home.home.dto;

import java.time.LocalDate;

public record ActivityHeatmapDto(
        LocalDate date,
        int count
) {}
