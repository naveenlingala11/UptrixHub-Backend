package com.ja.user.dto;

import com.ja.user.entity.UserSkill;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSkillResponse {

    private Long id;
    private String name;
    private int proficiency;

    public static UserSkillResponse from(UserSkill s) {
        return new UserSkillResponse(
                s.getId(),
                s.getName(),
                s.getProficiency()
        );
    }
}
