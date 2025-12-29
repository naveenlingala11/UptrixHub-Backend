package com.ja.payment.entity;

import com.ja.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ================= APP ORDER ================= */

    @Column(nullable = false)
    private Long orderId;   // App Order ID (orders.id)

    /* ================= RAZORPAY ================= */

    @Column(nullable = false, unique = true)
    private String razorpayOrderId;

    private String razorpayPaymentId;

    @Column(length = 512)
    private String razorpaySignature;

    /* ================= STATUS ================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;  // CREATED | SUCCESS | FAILED | REFUNDED

    private int amount;

    private Instant createdAt;
}
