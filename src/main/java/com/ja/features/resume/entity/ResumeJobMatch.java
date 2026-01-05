package com.ja.features.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resume_job_match")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeJobMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 4000)
    private String jobDescription;

    private String targetRole;

    private int matchScore;

    private int skillMatchScore;
    private int experienceMatchScore;
    private int techStackMatchScore;
    private int atsMatchScore;

    @ElementCollection
    private List<String> matchedSkills;

    @ElementCollection
    private List<String> missingSkills;

    @ElementCollection
    private List<String> suggestions;

    @ElementCollection
    private List<String> mockInterviewTopics;

    private String resumeUrl;

    private LocalDateTime analyzedAt;
}
