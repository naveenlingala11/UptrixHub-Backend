package com.ja.home.kit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "user_kit_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "kit_code"})
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKitProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // CORE_JAVA, SPRING_BOOT, MICROSERVICES
    @Column(name = "kit_code", nullable = false)
    private String kitCode;

    @Column(nullable = false)
    private int progress; // 0–100

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean purchased;
}
