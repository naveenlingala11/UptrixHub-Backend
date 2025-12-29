package com.ja.order.controller;

import com.ja.order.dto.CreateOrderRequest;
import com.ja.order.entity.Order;
import com.ja.order.enums.OrderStatus;
import com.ja.order.repository.OrderRepository;
import com.ja.order.service.OrderService;
import com.ja.security.JwtUserPrincipal;
import com.ja.user.entity.User;
import com.ja.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    /* ================= CREATE ORDER ================= */
    @PostMapping
    public Order create(
            @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return orderService.createOrder(request, principal.getUserId());
    }

    /* ================= GET ORDER ================= */
    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    /* ================= MY ORDERS ================= */
    @GetMapping("/my")
    public List<Order> myOrders(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return orderService.getUserOrders(principal.getUserId());
    }

    /* ================= VERIFY ORDER ================= */
    @GetMapping("/{id}/status")
    public OrderStatus status(@PathVariable Long id) {
        return orderService.getOrder(id).getStatus();
    }

    /* ================= CANCEL ORDER ================= */
    @PostMapping("/{id}/cancel")
    public void cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        orderService.cancelOrder(id, principal.getUserId());
    }

    @GetMapping("/pending")
    public List<Order> pending(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return orderRepository.findByUserIdAndStatus(
                principal.getUserId(),
                OrderStatus.PENDING
        );
    }

}
