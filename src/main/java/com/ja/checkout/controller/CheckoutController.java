package com.ja.checkout.controller;

import com.ja.checkout.dto.CheckoutRequest;
import com.ja.checkout.dto.CheckoutResponse;
import com.ja.checkout.service.CheckoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping("/preview")
    public CheckoutResponse preview(@RequestBody CheckoutRequest req) {
        return service.preview(req);
    }
}
