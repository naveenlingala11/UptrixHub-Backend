package com.ja.payment.entity;

import com.ja.course.entity.Course;
import com.ja.payment.enums.PaymentStatus;
import com.ja.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "order_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /* ================= RELATIONS ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /* ================= ORDER ================= */

    @Column(nullable = false)
    private Long orderId; // ✅ APP ORDER ID (for invoices)

    /* ================= PAYMENT ================= */

    @Column(nullable = false)
    private String razorpayOrderId;   // Razorpay order_id

    @Column(nullable = false)
    private String razorpayPaymentId; // Razorpay payment_id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;     // SUCCESS | FAILED | REFUNDED

    private LocalDateTime purchasedAt;
}
