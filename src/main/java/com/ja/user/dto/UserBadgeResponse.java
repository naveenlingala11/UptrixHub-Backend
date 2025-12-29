package com.ja.user.dto;

import com.ja.user.entity.UserBadge;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserBadgeResponse {

    private Long id;
    private String name;
    private String icon;
    private LocalDate earnedAt;

    public static UserBadgeResponse from(UserBadge b) {
        return new UserBadgeResponse(
                b.getId(),
                b.getName(),
                b.getIcon(),
                b.getEarnedAt()
        );
    }
}
