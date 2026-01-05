package com.ja.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopUserXp {
    private Long userId;
    private String name;
    private Integer totalXp;
}
