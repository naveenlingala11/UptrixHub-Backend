package com.ja.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileStats {
    private int skills;
    private int achievements;
    private int badges;
}
