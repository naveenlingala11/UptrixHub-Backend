package com.ja.features.resume.controller;

import com.ja.features.resume.dto.ResumeJobMatchDto;
import com.ja.features.resume.dto.ResumeUploadResponse;
import com.ja.features.resume.service.ResumeAnalysisService;
import com.ja.features.resume.service.ResumeJobMatchService;
import com.ja.features.resume.service.ResumeUploadService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/resume-upload")
@RequiredArgsConstructor
public class ResumeUploadController {

    private final ResumeUploadService uploadService;
    private final ResumeAnalysisService resumeAnalysisService;
    private final ResumeJobMatchService resumeJobMatchService;

    /* ================= UPLOAD ONLY ================= */

    @PostMapping
    public ResumeUploadResponse upload(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestParam("file") MultipartFile file
    ) {
        return uploadService.upload(user.getUserId(), file);
    }

    /* ================= UPLOAD + ATS ANALYSIS ================= */

    @PostMapping("/analyze-with-upload")
    public ResumeJobMatchDto analyzeWithUpload(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("targetRole") String targetRole
    ) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "JWT missing or invalid"
            );
        }

        String resumeText =
                uploadService.uploadAndExtract(user.getUserId(), file);

        resumeAnalysisService.analyzeFromText(
                user.getUserId(),
                resumeText,
                targetRole
        );

        return resumeJobMatchService.analyzeFromText(
                user.getUserId(),
                resumeText,
                jobDescription,
                targetRole
        );
    }

}
