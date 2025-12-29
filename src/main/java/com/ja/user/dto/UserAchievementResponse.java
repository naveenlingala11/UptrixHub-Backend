package com.ja.user.dto;

import com.ja.user.entity.UserAchievement;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserAchievementResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate achievedAt;

    public static UserAchievementResponse from(UserAchievement a) {
        return new UserAchievementResponse(
                a.getId(),
                a.getTitle(),
                a.getDescription(),
                a.getAchievedAt()
        );
    }
}
