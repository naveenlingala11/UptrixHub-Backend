package com.ja.checkout.dto;

import java.util.List;

public record CheckoutRequest(
        List<String> courseIds,
        Long bundleId,
        String coupon
) {}
