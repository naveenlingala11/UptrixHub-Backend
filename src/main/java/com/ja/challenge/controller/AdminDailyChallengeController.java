package com.ja.challenge.controller;

import com.ja.challenge.dto.AdminDailyChallengeRequest;
import com.ja.challenge.service.AdminDailyChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/daily-challenge")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDailyChallengeController {

    private final AdminDailyChallengeService service;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void upload(
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = false) String json
    ) {
        service.upsert(file, json);
    }
}
