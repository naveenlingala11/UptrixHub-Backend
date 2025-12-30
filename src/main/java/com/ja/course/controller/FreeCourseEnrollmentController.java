package com.ja.course.controller;

import com.ja.course.entity.Course;
import com.ja.course.enums.PriceType;
import com.ja.course.repository.CourseRepository;
import com.ja.payment.entity.OrderItem;
import com.ja.payment.enums.PaymentStatus;
import com.ja.payment.repository.OrderItemRepository;
import com.ja.security.JwtUserPrincipal;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class FreeCourseEnrollmentController {

    private final CourseRepository courseRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @PostMapping("/{courseId}/enroll-free")
    public void enrollFree(
            @PathVariable String courseId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getPriceType() != PriceType.FREE) {
            throw new RuntimeException("Not a free course");
        }

        if (orderItemRepository.existsByUser_IdAndCourse_Id(
                principal.getUserId(), courseId)) {
            return; // already enrolled
        }

        orderItemRepository.save(
                OrderItem.builder()
                        .user(userRepository.getReferenceById(principal.getUserId()))
                        .course(course)
                        .orderId(0L)
                        .razorpayOrderId("FREE")
                        .razorpayPaymentId("FREE")
                        .status(PaymentStatus.SUCCESS)
                        .purchasedAt(LocalDateTime.now())
                        .build()
        );
    }
}
