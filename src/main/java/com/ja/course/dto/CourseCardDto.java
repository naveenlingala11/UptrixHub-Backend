package com.ja.course.dto;

import com.ja.course.entity.Course;
import com.ja.course.enums.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCardDto {

    private String id;
    private String title;
    private String description;
    private PriceType priceType;
    private int price;
    private boolean unlocked;

    /* ================= FACTORY ================= */

    // For public listings
    public static CourseCardDto publicView(Course course) {
        return new CourseCardDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPriceType(),
                course.getPrice(),
                false
        );
    }

    // For purchased courses
    public static CourseCardDto unlockedView(Course course) {
        return new CourseCardDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPriceType(),
                course.getPrice(),
                true
        );
    }
}
