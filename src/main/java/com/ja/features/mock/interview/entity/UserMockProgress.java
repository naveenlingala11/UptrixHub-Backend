package com.ja.features.mock.interview.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_mock_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMockProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int totalRounds;
    private int attemptedRounds;
    private int score;

    private LocalDateTime lastAttempt;

    @ElementCollection
    private List<String> strengths;

    @ElementCollection
    private List<String> weaknesses;
}
