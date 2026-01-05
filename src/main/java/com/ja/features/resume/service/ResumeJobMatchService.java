package com.ja.features.resume.service;

import com.ja.features.resume.dto.*;
import com.ja.features.resume.engine.RoleWeightService;
import com.ja.features.resume.engine.SkillExtractor;
import com.ja.features.resume.engine.SkillRepository;
import com.ja.features.resume.entity.ResumeJobMatch;
import com.ja.features.resume.repository.ResumeJobMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ResumeJobMatchService {

    private final ResumeJobMatchRepository repo;
    private final SkillRepository skillRepo;
    private final RoleWeightService roleWeightService;

    public ResumeJobMatchDto analyze(Long userId, ResumeJobMatchRequest req) {

        // 🔹 STEP 1: Extract skills from JD (later NLP / OpenAI)
        List<String> jdSkills = List.of(
                "Java", "Spring Boot", "Microservices",
                "Kafka", "System Design"
        );

        // 🔹 STEP 2: Extract resume skills (later NLP)
        List<String> resumeSkills = List.of(
                "Java", "Spring Boot", "REST", "MySQL"
        );

        // 🔹 STEP 3: Compare
        List<String> matched = resumeSkills.stream()
                .filter(jdSkills::contains)
                .toList();

        List<String> missing = jdSkills.stream()
                .filter(s -> !resumeSkills.contains(s))
                .toList();

        // 🔹 STEP 4: Scoring
        int skillScore = (matched.size() * 100) / jdSkills.size();
        int expScore = missing.contains("System Design") ? 60 : 85;
        int techScore = skillScore;
        int atsScore = 70;

        int finalScore =
                (skillScore * 40 +
                        expScore * 25 +
                        techScore * 25 +
                        atsScore * 10) / 100;

        ResumeJobMatch entity = ResumeJobMatch.builder()
                .userId(userId)
                .jobDescription(req.jobDescription())
                .targetRole(req.targetRole())
                .matchScore(finalScore)
                .skillMatchScore(skillScore)
                .experienceMatchScore(expScore)
                .techStackMatchScore(techScore)
                .atsMatchScore(atsScore)
                .matchedSkills(matched)
                .missingSkills(missing)
                .suggestions(List.of(
                        "Add Microservices project",
                        "Prepare Kafka use cases",
                        "Practice System Design interviews"
                ))
                .mockInterviewTopics(missing)
                .resumeUrl(req.resumeUrl())
                .analyzedAt(LocalDateTime.now())
                .build();

        repo.save(entity);

        return new ResumeJobMatchDto(
                finalScore,
                skillScore,
                expScore,
                techScore,
                atsScore,
                matched,
                missing,
                entity.getSuggestions(),
                entity.getMockInterviewTopics()
        );
    }

    public ResumeJobMatchDto latest(Long userId) {
        return repo.findTopByUserIdOrderByAnalyzedAtDesc(userId)
                .map(r -> new ResumeJobMatchDto(
                        r.getMatchScore(),
                        r.getSkillMatchScore(),
                        r.getExperienceMatchScore(),
                        r.getTechStackMatchScore(),
                        r.getAtsMatchScore(),
                        r.getMatchedSkills(),
                        r.getMissingSkills(),
                        r.getSuggestions(),
                        r.getMockInterviewTopics()
                ))
                .orElse(null);
    }

    public ResumeJobMatchDto analyzeFromText(
            Long userId,
            String resumeText,
            String jobDescription,
            String role
    ) {

        Set<String> jdSkills = skillRepo.extract(jobDescription);
        Set<String> resumeSkills = skillRepo.extract(resumeText);

        List<String> matched = jdSkills.stream()
                .filter(resumeSkills::contains)
                .sorted()
                .toList();

        List<String> missing = jdSkills.stream()
                .filter(s -> !resumeSkills.contains(s))
                .sorted()
                .toList();

        int skillScore = percent(matched.size(), jdSkills.size());
        int atsScore = atsScore(jdSkills, resumeText);
        int expScore = missing.size() > 5 ? 60 : 85;
        int techScore = skillScore;

        int finalScore =
                (skillScore * 40 +
                        expScore * 25 +
                        techScore * 25 +
                        atsScore * 10) / 100;

        List<String> suggestions = missing.stream()
                .map(s -> "Add a resume bullet showing hands-on experience with " + s)
                .toList();

        ResumeJobMatch entity = ResumeJobMatch.builder()
                .userId(userId)
                .jobDescription(jobDescription)
                .targetRole(role)
                .matchScore(finalScore)
                .skillMatchScore(skillScore)
                .experienceMatchScore(expScore)
                .techStackMatchScore(techScore)
                .atsMatchScore(atsScore)
                .matchedSkills(matched)
                .missingSkills(missing)
                .suggestions(suggestions)
                .mockInterviewTopics(missing)
                .resumeUrl("UPLOAD")
                .analyzedAt(LocalDateTime.now())
                .build();

        repo.save(entity);

        return new ResumeJobMatchDto(
                finalScore,
                skillScore,
                expScore,
                techScore,
                atsScore,
                matched,
                missing,
                suggestions,
                missing
        );
    }

    private int atsScore(Set<String> jdSkills, String resumeText) {
        int hits = 0;
        String text = resumeText.toLowerCase();
        for (String s : jdSkills) {
            if (text.contains(s.toLowerCase())) hits++;
        }
        return percent(hits, jdSkills.size());
    }

    private int percent(int a, int b) {
        return b == 0 ? 0 : (a * 100) / b;
    }

}
