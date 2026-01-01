package com.ja.pseudo.controller;

import com.ja.pseudo.dto.QuestionUploadResponse;
import com.ja.pseudo.service.PseudoQuestionUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/pseudo/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPseudoQuestionController {

    private final PseudoQuestionUploadService service;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public QuestionUploadResponse upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("skill") String skill
    ) throws Exception {
        return service.upload(file, skill);
    }

}

