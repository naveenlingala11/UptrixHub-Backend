package com.ja.features.mock.interview.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mock_interview_request")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockInterviewRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String role;

    @ElementCollection
    @CollectionTable(name = "mock_request_slots", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "slot")
    private List<String> preferredSlots;

    private String platform; // ZOOM / GOOGLE_MEET

    private String status;   // REQUESTED / SCHEDULED / COMPLETED / CANCELLED

    private LocalDateTime createdAt;
}
