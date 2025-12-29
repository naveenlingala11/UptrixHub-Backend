package com.ja.payment.repository;

import com.ja.payment.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    /* ================= ACCESS CHECK ================= */
    boolean existsByUser_IdAndCourse_Id(Long userId, String courseId);

    /* ================= USER PURCHASES ================= */
    List<OrderItem> findByUser_Id(Long userId);

    /* ================= INVOICE ================= */
    List<OrderItem> findByOrderId(Long orderId); // ✅ BACK & VALID
}
