package com.ja.payment.dto;

public record RazorpayVerifyRequest(
        String razorpayOrderId,
        String razorpayPaymentId,
        String razorpaySignature
) {}
