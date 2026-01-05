package com.ja.features.resume.repository;

import com.ja.features.resume.entity.ResumeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeDocumentRepository
        extends JpaRepository<ResumeDocument, Long> {

    Optional<ResumeDocument> findTopByUserIdOrderByUploadedAtDesc(Long userId);
}
