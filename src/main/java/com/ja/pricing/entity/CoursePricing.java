package com.ja.pricing.entity;

import com.ja.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_pricing")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CoursePricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private int basePrice;      // 199, 299 etc
    private int discountPrice;  // nullable
    private boolean active;
}
