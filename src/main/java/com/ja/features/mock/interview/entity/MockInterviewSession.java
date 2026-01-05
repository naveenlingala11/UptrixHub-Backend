package com.ja.features.mock.interview.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mock_interview_session")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockInterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestId;

    private Long interviewerId;

    private String meetingLink;

    private LocalDateTime scheduledAt;

    private int duration; // minutes

    private String status; // UPCOMING / COMPLETED

    private Integer score;

    @ElementCollection
    private List<String> strengths;

    @ElementCollection
    private List<String> weaknesses;

    @Column(length = 2000)
    private String review;

    private LocalDateTime createdAt;
}
