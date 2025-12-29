package com.ja.user.dto;

import com.ja.user.entity.User;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String avatar;
    private String role;
    private String provider;
    private String subscription;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobile(),
                user.getAvatar(),
                user.getRole() != null ? user.getRole().name() : "USER",
                user.getProvider() != null ? user.getProvider().name() : "LOCAL",
                user.getSubscription() != null ? user.getSubscription().name() : "FREE"
        );
    }

}
