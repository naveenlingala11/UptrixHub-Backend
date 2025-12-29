package com.ja.order.entity;

import com.ja.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int subtotal;
    private int gst;
    private int discount;
    private int total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt;
}
