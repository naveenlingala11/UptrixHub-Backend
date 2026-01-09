package com.ja.games.games.bughunter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BulkUploadReport {
    private int total;
    private int saved;
    private String status; // DRAFT | PUBLISHED
}
