package com.ja.order.repository;

import com.ja.order.entity.Order;
import com.ja.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByUserIdAndStatus(Long id, OrderStatus orderStatus);
}

