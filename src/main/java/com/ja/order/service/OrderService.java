package com.ja.order.service;

import com.ja.checkout.dto.CheckoutRequest;
import com.ja.checkout.dto.CheckoutResponse;
import com.ja.checkout.service.CheckoutService;
import com.ja.course.entity.Course;
import com.ja.course.repository.CourseRepository;
import com.ja.order.dto.CreateOrderRequest;
import com.ja.order.entity.Order;
import com.ja.order.enums.OrderStatus;
import com.ja.order.repository.OrderRepository;
import com.ja.payment.entity.OrderItem;
import com.ja.payment.enums.PaymentStatus;
import com.ja.payment.repository.OrderItemRepository;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepo;
    private final CourseRepository courseRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    /* ================= CREATE ORDER ================= */
    public Order createOrder(CreateOrderRequest req, Long userId) {

        CheckoutResponse preview =
                checkoutService.preview(
                        new CheckoutRequest(
                                req.getCourseIds(),
                                null,
                                req.getCouponCode()
                        )
                );

        Order order = Order.builder()
                .userId(userId)
                .subtotal(preview.subtotal())
                .gst(preview.gst())
                .discount(preview.discount())
                .total(preview.total())
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        order = orderRepo.save(order); // 🔥 save first to get orderId

        // ================= CREATE ORDER ITEMS (🔥 IMPORTANT) =================
        for (String courseId : req.getCourseIds()) {

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            orderItemRepository.save(
                    OrderItem.builder()
                            .user(userRepository.getReferenceById(userId))
                            .course(course)
                            .orderId(order.getId())
                            .razorpayOrderId("PENDING")
                            .razorpayPaymentId("PENDING")
                            .status(PaymentStatus.CREATED)
                            .build()
            );
        }

        return order; // ✅ return LAST
    }

    /* ================= GET ORDER ================= */
    public Order getOrder(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /* ================= USER ORDERS ================= */
    public List<Order> getUserOrders(Long userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /* ================= MARK PAID ================= */
    public void markPaid(Long orderId) {
        Order order = getOrder(orderId);
        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);
    }

    /* ================= CANCEL ORDER ================= */
    public void cancelOrder(Long orderId, Long userId) {

        Order order = getOrder(orderId);

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Paid order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }

}
