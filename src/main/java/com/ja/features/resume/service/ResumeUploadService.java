package com.ja.features.resume.service;

import com.ja.features.resume.dto.ResumeUploadResponse;
import com.ja.features.resume.entity.ResumeDocument;
import com.ja.features.resume.repository.ResumeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResumeUploadService {

    private final ResumeDocumentRepository repo;

    // ✅ ENV / PROPERTIES BASED DIRECTORY
    @Value("${app.uploads.resume-dir}")
    private String resumeDir;

    /* ================= PUBLIC ================= */

    public ResumeUploadResponse upload(Long userId, MultipartFile file) {
        return uploadInternal(userId, file);
    }


    public String uploadAndExtract(Long userId, MultipartFile file) {

        try {
            Path base = Paths.get(resumeDir);
            Files.createDirectories(base);

            String safe = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            Path path = base.resolve(userId + "_" + System.currentTimeMillis() + "_" + safe);

            file.transferTo(path);

            String text;
            try (PDDocument doc = PDDocument.load(path.toFile())) {
                text = new PDFTextStripper().getText(doc);
            }

            ResumeDocument entity = ResumeDocument.builder()
                    .userId(userId)
                    .fileName(safe)
                    .filePath(path.toString())
                    .extractedText(text)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            repo.save(entity);
            return text;

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Resume processing failed"
            );
        }
    }

    /* ================= INTERNAL ================= */

    private ResumeUploadResponse uploadInternal(Long userId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Resume file is missing or empty"
            );
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".pdf")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only PDF resumes are supported"
            );
        }

        try {
            // ✅ CREATE BASE DIRECTORY IF NOT EXISTS
            Path baseDir = Paths.get(resumeDir);
            Files.createDirectories(baseDir);

            // ✅ SAFE FILE NAME
            String safeName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = userId + "_" + System.currentTimeMillis() + "_" + safeName;

            Path filePath = baseDir.resolve(fileName);

            // ✅ SAVE FILE
            file.transferTo(filePath.toFile());

            // ✅ EXTRACT TEXT
            String extractedText;
            try (PDDocument document = PDDocument.load(filePath.toFile())) {
                PDFTextStripper stripper = new PDFTextStripper();
                extractedText = stripper.getText(document);
            }

            if (extractedText == null || extractedText.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Unable to extract text from PDF"
                );
            }

            // ✅ SAVE DB
            ResumeDocument entity = ResumeDocument.builder()
                    .userId(userId)
                    .fileName(originalName)
                    .filePath(filePath.toAbsolutePath().toString())
                    .extractedText(extractedText)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            repo.save(entity);

            return new ResumeUploadResponse(
                    originalName,
                    extractedText.length(),
                    extractedText.substring(0, Math.min(400, extractedText.length())),
                    extractedText
            );

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 keep this
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Resume processing failed"
            );
        }
    }

    /* ================= HELPERS ================= */

    public ResumeDocument latestDocument(Long userId) {
        return repo.findTopByUserIdOrderByUploadedAtDesc(userId)
                .orElse(null);
    }

}
