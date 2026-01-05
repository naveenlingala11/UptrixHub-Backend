package com.ja.home.learning.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_continue_learning")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContinueLearning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // CORE_JAVA, SPRING_BOOT
    @Column(nullable = false)
    private String kitCode;

    // e.g. "Java Collections"
    @Column(nullable = false)
    private String lastTopic;

    // frontend resume route
    @Column(nullable = false)
    private String resumeRoute;

    // current streak days
    @Column(nullable = false)
    private int streakDays;
}
