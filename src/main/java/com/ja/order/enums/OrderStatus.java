package com.ja.order.enums;

public enum OrderStatus {
    CREATED,     // Order created, before checkout
    PENDING,     // Payment initiated, awaiting confirmation
    PAID,        // Payment successful
    FAILED,      // Payment failed
    CANCELLED    // Cancelled by user/admin
}
