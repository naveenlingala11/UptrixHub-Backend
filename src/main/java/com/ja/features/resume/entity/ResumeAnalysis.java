package com.ja.features.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resume_analysis")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String targetRole;

    private int score;
    private int jobMatchScore;

    private int skillScore;
    private int projectScore;
    private int experienceScore;
    private int atsScore;
    private int keywordScore;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> strengths;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> missingSkills;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> suggestions;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> atsKeywords;

    private String resumeUrl;
    private LocalDateTime analyzedAt;
}
