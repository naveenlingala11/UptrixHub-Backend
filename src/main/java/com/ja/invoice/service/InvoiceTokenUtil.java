package com.ja.invoice.service;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceTokenUtil {

    @Value("${invoice.secret}")
    private String secret;

    public String generate(Long invoiceId, Long userId) {
        return HmacUtils.hmacSha256Hex(
                secret,
                invoiceId + ":" + userId
        );
    }

    public boolean verify(Long invoiceId, Long userId, String token) {
        return generate(invoiceId, userId).equals(token);
    }
}

