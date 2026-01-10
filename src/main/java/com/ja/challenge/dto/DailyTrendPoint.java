package com.ja.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyTrendPoint {
    private String date;
    private long submissions;
}
