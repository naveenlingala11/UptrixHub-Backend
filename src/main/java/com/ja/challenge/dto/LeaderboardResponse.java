package com.ja.challenge.dto;

import lombok.Data;

import java.util.List;

@Data
public class LeaderboardResponse {

    private String type; // DAILY / GLOBAL
    private List<LeaderboardEntryDto> leaders;
}
