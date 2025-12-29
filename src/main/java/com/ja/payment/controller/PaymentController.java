package com.ja.payment.controller;

import com.ja.course.entity.Course;
import com.ja.course.repository.CourseRepository;
import com.ja.order.entity.Order;
import com.ja.order.repository.OrderRepository;
import com.ja.payment.dto.CreateRazorpayOrderResponse;
import com.ja.payment.dto.PaymentSuccessRequest;
import com.ja.payment.dto.RazorpayVerifyRequest;
import com.ja.payment.entity.OrderItem;
import com.ja.payment.enums.PaymentStatus;
import com.ja.payment.repository.OrderItemRepository;
import com.ja.payment.service.RazorpayPaymentService;
import com.ja.user.entity.User;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayPaymentService paymentService;
    private final OrderRepository orderRepo;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final CourseRepository courseRepository;

    @PostMapping("/razorpay/create")
    public CreateRazorpayOrderResponse create(@RequestParam Long orderId)
            throws Exception {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return paymentService.createOrder(order);
    }

    @PostMapping("/razorpay/verify")
    public void verify(@RequestBody RazorpayVerifyRequest req)
            throws Exception {
        paymentService.verifyPayment(req);
    }

    @PostMapping("/success")
    public ResponseEntity<?> paymentSuccess(
            @RequestBody PaymentSuccessRequest req,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Long userId = Long.valueOf(principal.getUsername()); // 👈 sub = userId

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (orderItemRepository.existsByUser_IdAndCourse_Id(
                user.getId(), course.getId())) {

            return ResponseEntity
                    .badRequest()
                    .body("Course already purchased");
        }

        OrderItem orderItem = OrderItem.builder()
                .user(user)
                .course(course)
                .razorpayOrderId(req.razorpayOrderId())
                .razorpayPaymentId(req.razorpayPaymentId())
                .status(PaymentStatus.SUCCESS)
                .purchasedAt(LocalDateTime.now())
                .build();


        orderItemRepository.save(orderItem);

        return ResponseEntity.ok(Map.of(
                "message", "Payment successful",
                "courseUnlocked", true
        ));
    }

}
