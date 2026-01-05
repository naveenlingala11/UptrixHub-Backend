package com.ja.features.resume.repository;

import com.ja.features.resume.entity.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeAnalysisRepository
        extends JpaRepository<ResumeAnalysis, Long> {

    Optional<ResumeAnalysis> findTopByUserIdOrderByAnalyzedAtDesc(Long userId);

    List<ResumeAnalysis> findByUserIdOrderByAnalyzedAtDesc(Long userId);
}
