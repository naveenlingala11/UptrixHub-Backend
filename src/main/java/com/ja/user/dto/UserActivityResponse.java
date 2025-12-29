package com.ja.user.dto;

import com.ja.user.entity.UserActivity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserActivityResponse {

    private Long id;
    private String type;
    private String message;
    private LocalDateTime createdAt;

    public static UserActivityResponse from(UserActivity a) {
        return new UserActivityResponse(
                a.getId(),
                a.getType(),
                a.getMessage(),
                a.getCreatedAt()
        );
    }
}
