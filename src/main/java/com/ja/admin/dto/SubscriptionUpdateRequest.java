package com.ja.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubscriptionUpdateRequest {
    private boolean enable;  // true = enable PRO
    private int graceDays;   // 7 / 14 / 41
}
