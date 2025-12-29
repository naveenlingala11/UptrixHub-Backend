package com.ja.course.service;

import com.ja.course.entity.Course;
import com.ja.course.enums.PriceType;
import com.ja.payment.repository.OrderItemRepository;
import com.ja.user.entity.User;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseAccessService {

    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    /* ==================================================
       CHECK ACCESS USING COURSE ENTITY
       ================================================== */
    public boolean canAccess(Long userId, Course course) {

        // ✅ FREE COURSE
        if (course.getPriceType() == PriceType.FREE) {
            return true;
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // ✅ PRO SUBSCRIPTION
        if (Boolean.TRUE.equals(user.getSubscriptionActive())) {
            return true;
        }

        // ✅ PURCHASED COURSE
        return orderItemRepository
                .existsByUser_IdAndCourse_Id(userId, course.getId());
    }

    /* ==================================================
       CHECK ACCESS USING COURSE ID
       ================================================== */
    public boolean hasAccess(Long userId, String courseId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        if (Boolean.TRUE.equals(user.getSubscriptionActive())) {
            return true;
        }

        return orderItemRepository
                .existsByUser_IdAndCourse_Id(userId, courseId);
    }
}
