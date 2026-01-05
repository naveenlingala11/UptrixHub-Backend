package com.ja.features.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume_document")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fileName;
    private String filePath;

    @Column(length = 10000)
    private String extractedText;

    private LocalDateTime uploadedAt;
}
