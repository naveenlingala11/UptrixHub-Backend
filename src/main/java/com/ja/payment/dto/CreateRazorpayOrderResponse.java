package com.ja.payment.dto;

public record CreateRazorpayOrderResponse(
        String razorpayOrderId,
        int amount,
        String currency,
        String keyId
) {}
