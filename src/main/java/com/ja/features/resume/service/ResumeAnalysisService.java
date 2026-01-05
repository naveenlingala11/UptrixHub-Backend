package com.ja.features.resume.service;

import com.ja.features.resume.dto.*;
import com.ja.features.resume.entity.ResumeAnalysis;
import com.ja.features.resume.repository.ResumeAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ResumeAnalysisService {

    private final ResumeAnalysisRepository repo;

    public ResumeAnalysisDto analyze(Long userId, ResumeUploadRequest req) {

        // ⚠️ Replace with NLP / OpenAI later
        List<String> resumeSkills = List.of("Java", "Spring Boot", "REST", "MySQL");

        List<String> required = List.of(
                "System Design", "Microservices", "Kafka"
        );

        List<String> missing = required.stream()
                .filter(r -> !resumeSkills.contains(r))
                .toList();

        int skill = rand(22, 30);
        int project = rand(18, 25);
        int experience = rand(12, 20);
        int ats = rand(10, 15);
        int keyword = rand(6, 10);

        int total = skill + project + experience + ats + keyword;
        int jobMatch = 100 - (missing.size() * 15);

        ResumeAnalysis entity = ResumeAnalysis.builder()
                .userId(userId)
                .targetRole(req.targetRole())
                .score(total)
                .jobMatchScore(jobMatch)
                .skillScore(skill)
                .projectScore(project)
                .experienceScore(experience)
                .atsScore(ats)
                .keywordScore(keyword)
                .strengths(List.of("Strong Java", "Good Spring Boot"))
                .missingSkills(missing)
                .suggestions(List.of(
                        "Add system design projects",
                        "Mention scalability",
                        "Add Kafka experience"
                ))
                .atsKeywords(List.of("Java", "Spring Boot", "REST"))
                .resumeUrl(req.resumeUrl())
                .analyzedAt(LocalDateTime.now())
                .build();

        repo.save(entity);

        return map(entity);
    }

    public ResumeAnalysisDto latest(Long userId) {
        return repo.findTopByUserIdOrderByAnalyzedAtDesc(userId)
                .map(this::map)
                .orElse(null);
    }

    public ResumeCompareDto compare(Long userId) {
        List<ResumeAnalysis> list = repo.findByUserIdOrderByAnalyzedAtDesc(userId);
        if (list.size() < 2) return null;

        return new ResumeCompareDto(
                list.get(1).getScore(),
                list.get(0).getScore(),
                list.get(0).getScore() - list.get(1).getScore()
        );
    }

    private ResumeAnalysisDto map(ResumeAnalysis r) {
        return new ResumeAnalysisDto(
                r.getScore(),
                r.getJobMatchScore(),
                r.getSkillScore(),
                r.getProjectScore(),
                r.getExperienceScore(),
                r.getAtsScore(),
                r.getKeywordScore(),
                r.getStrengths(),
                r.getMissingSkills(),
                r.getSuggestions(),
                r.getAtsKeywords()
        );
    }

    private int rand(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public void analyzeFromText(
            Long userId,
            String resumeText,
            String targetRole
    ) {

        // 🔹 Extract skills (simple ATS logic)
        List<String> resumeSkills = extractSkills(resumeText);

        List<String> required = List.of(
                "System Design", "Microservices", "Kafka"
        );

        List<String> missing = required.stream()
                .filter(r -> !resumeSkills.contains(r))
                .toList();

        int skill = rand(22, 30);
        int project = rand(18, 25);
        int experience = rand(12, 20);
        int ats = rand(10, 15);
        int keyword = rand(6, 10);

        int total = skill + project + experience + ats + keyword;
        int jobMatch = Math.max(40, 100 - (missing.size() * 15));

        ResumeAnalysis entity = ResumeAnalysis.builder()
                .userId(userId)
                .targetRole(targetRole)
                .score(total)
                .jobMatchScore(jobMatch)
                .skillScore(skill)
                .projectScore(project)
                .experienceScore(experience)
                .atsScore(ats)
                .keywordScore(keyword)
                .strengths(resumeSkills)
                .missingSkills(missing)
                .suggestions(List.of(
                        "Add system design examples",
                        "Mention scalability metrics",
                        "Include Kafka use cases"
                ))
                .atsKeywords(resumeSkills)
                .resumeUrl("UPLOAD")
                .analyzedAt(LocalDateTime.now())
                .build();

        repo.save(entity);
    }

    private List<String> extractSkills(String text) {
        List<String> known = List.of(
                "Java", "Spring Boot", "REST",
                "Microservices", "Kafka", "MySQL"
        );

        return known.stream()
                .filter(s -> text.toLowerCase().contains(s.toLowerCase()))
                .toList();
    }

    public ResumeHomeSummaryDto homeSummary(Long userId) {

        return repo.findTopByUserIdOrderByAnalyzedAtDesc(userId)
                .map(r -> new ResumeHomeSummaryDto(
                        r.getScore(),
                        r.getScore() >= 80 ? "Strong Resume"
                                : r.getScore() >= 60 ? "Average Resume"
                                : "Needs Improvement",
                        r.getStrengths(),
                        r.getMissingSkills()
                ))
                .orElse(new ResumeHomeSummaryDto(
                        0,
                        "Upload Resume",
                        List.of(),
                        List.of()
                ));
    }

    @Transactional(readOnly = true)
    public ResumeHomeDetailDto homeDetail(Long userId) {
        ResumeAnalysis analysis =
                repo.findTopByUserIdOrderByAnalyzedAtDesc(userId).orElse(null);

        if (analysis == null) {
            return new ResumeHomeDetailDto(
                    0, "Upload Resume", null,
                    List.of(), List.of(), List.of()
            );
        }

        return new ResumeHomeDetailDto(
                analysis.getScore(),
                analysis.getScore() >= 80 ? "Strong Resume"
                        : analysis.getScore() >= 60 ? "Average Resume"
                        : "Needs Improvement",
                analysis.getAnalyzedAt(),
                analysis.getStrengths(),
                analysis.getSuggestions(),
                analysis.getAtsKeywords()
        );
    }

}
