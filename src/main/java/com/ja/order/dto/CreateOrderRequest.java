package com.ja.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {

    // Example: ["CORE_JAVA", "SPRING_BOOT"]
    private List<String> courseIds;

    // Example: "COMPLETE_JAVA"
    private String bundleCode;

    // Example: "JAVA200"
    private String couponCode;
}
