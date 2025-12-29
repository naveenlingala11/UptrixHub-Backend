package com.ja.checkout.dto;

import java.util.List;

public record CheckoutResponse(
        List<CheckoutItem> items,
        int subtotal,
        int discount,
        int gst,
        int total
) {}
