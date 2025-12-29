package com.ja.checkout.dto;

public record CheckoutItem(
        String courseId,
        String title,
        int price
) {}
