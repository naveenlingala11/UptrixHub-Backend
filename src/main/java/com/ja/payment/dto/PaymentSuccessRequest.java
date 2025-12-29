package com.ja.payment.dto;

public record PaymentSuccessRequest(
        String courseId,
        String razorpayOrderId,
        String razorpayPaymentId
) {}
