package com.ja.course.service;

import com.ja.course.dto.CourseCardDto;
import com.ja.payment.entity.OrderItem;
import com.ja.payment.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyCoursesService {

    private final OrderItemRepository orderItemRepository;

    public List<CourseCardDto> getMyCourses(Long userId) {

        return orderItemRepository.findByUser_Id(userId)
                .stream()
                .map(OrderItem::getCourse)
                .distinct()
                .map(CourseCardDto::unlockedView)
                .toList();
    }
}

