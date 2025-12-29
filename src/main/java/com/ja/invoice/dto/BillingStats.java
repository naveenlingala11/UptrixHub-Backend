package com.ja.invoice.dto;

public record BillingStats(
        int totalPaid,
        int totalInvoices,
        int gstCollected
) {}

