package com.ja.course.dto;

import com.ja.course.entity.Course;
import com.ja.course.enums.PriceType;
import lombok.Builder;

import java.util.Map;

@Builder
public record CourseDetailResponse(
        String id,
        String title,
        String category,
        String level,
        String description,
        PriceType priceType,
        int price,
        boolean unlocked
) {
    public static CourseDetailResponse from(Course course, boolean unlocked) {
        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .category(course.getCategory())
                .level(course.getLevel())
                .description(course.getDescription())
                .priceType(course.getPriceType())
                .price(course.getPrice())
                .unlocked(unlocked)
                .build();
    }
}
