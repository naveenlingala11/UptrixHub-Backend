package com.ja.pseudo.dto;

import com.ja.user.enums.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscriptionAnalyticsResponse {

    private Subscription subscription;
    private Long attempts;
}
