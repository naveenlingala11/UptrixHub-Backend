package com.ja.user.dto;

import lombok.Data;

@Data
public class AddSkillRequest {
    private String name;
    private int proficiency; // 1–100
}
