package com.ja.games.bughunter.controller;


import com.ja.games.bughunter.dto.BugCategoryAnalytics;
import com.ja.games.bughunter.dto.BugHunterQuestionRequest;
import com.ja.games.bughunter.dto.BugQuestionAccuracy;
import com.ja.games.bughunter.dto.BugQuestionAnalytics;
import com.ja.games.bughunter.entity.BugHunterQuestion;
import com.ja.games.bughunter.repository.BugHunterAttemptRepository;
import com.ja.games.bughunter.repository.BugHunterQuestionRepository;
import com.ja.games.bughunter.service.BugHunterAdminService;
import com.ja.games.bughunter.service.BugHunterBulkValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/bug-hunter")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BugHunterAdminController {

    private final BugHunterQuestionRepository repo;
    private final BugHunterBulkValidator validator;
    private final BugHunterAdminService adminService;
    private final BugHunterAttemptRepository attemptRepo;

    // 🔍 List all questions
    @GetMapping
    public List<BugHunterQuestion> all() {
        return repo.findAll();
    }

    // ➕ Add new question
    @PostMapping
    public BugHunterQuestion create(@RequestBody BugHunterQuestion q) {
        q.setActive(true);
        return repo.save(q);
    }

    @PatchMapping("/{id}/publish")
    public void publish(@PathVariable Long id) {

        BugHunterQuestion q = repo.findById(id).orElseThrow();

        // Unpublish older versions of same parent
        if (q.getParentId() != null) {
            repo.findByParentIdOrderByVersionDesc(q.getParentId())
                    .forEach(v -> {
                        v.setPublished(false);
                        repo.save(v);
                    });
        }

        q.setPublished(true);
        q.setActive(true);

        repo.save(q);
    }

    // ✏️ Update question
    @PutMapping("/{id}")
    public BugHunterQuestion update(
            @PathVariable Long id,
            @RequestBody BugHunterQuestion updated
    ) {
        BugHunterQuestion q = repo.findById(id).orElseThrow();

        q.setTitle(updated.getTitle());
        q.setLanguage(updated.getLanguage());
        q.setDifficulty(updated.getDifficulty());
        q.setCode(updated.getCode());
        q.setBugType(updated.getBugType());
        q.setReason(updated.getReason());
        q.setFix(updated.getFix());
        q.setXp(updated.getXp());
        q.setActive(updated.isActive());

        return repo.save(q);
    }

    // 🔁 Activate / Deactivate
    @PatchMapping("/{id}/toggle")
    public void toggle(@PathVariable Long id) {
        BugHunterQuestion q = repo.findById(id).orElseThrow();
        q.setActive(!q.isActive());
        repo.save(q);
    }

    @PostMapping("/bulk")
    public Map<String, Object> bulk(
            @RequestBody List<BugHunterQuestionRequest> questions,
            @RequestParam(defaultValue = "false") boolean publish
    ) {
        if (questions == null || questions.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Questions list is empty"
            );
        }

        Map<String, Object> validation = validator.validate(questions);

        if (!(Boolean) validation.get("valid")) {
            return Map.of(
                    "valid", false,
                    "errors", validation.get("errors")
            );
        }

        int saved = 0;

        for (BugHunterQuestionRequest q : questions) {

            BugHunterQuestion e = new BugHunterQuestion();

            e.setTitle(q.getTitle());
            e.setLanguage(q.getLanguage());
            e.setDifficulty(q.getDifficulty());
            e.setCode(q.getCode());
            e.setBugType(q.getBugType());
            e.setReason(q.getReason());
            e.setFix(q.getFix());
            e.setXp(q.getXp());

            // 🔥 CRITICAL FIX
            e.setVersion(1);
            e.setActive(true);
            e.setPublished(publish);

            // save once to get ID
            BugHunterQuestion savedEntity = repo.save(e);

            // 🔥 SET parentId = self
            savedEntity.setParentId(savedEntity.getId());
            repo.save(savedEntity);

            saved++;
        }

        return Map.of(
                "valid", true,
                "saved", saved,
                "total", questions.size(),
                "status", publish ? "PUBLISHED" : "DRAFT"
        );
    }

    @PostMapping("/{id}/new-version")
    public BugHunterQuestion newVersion(
            @PathVariable Long id,
            @RequestBody BugHunterQuestion updated
    ) {
        return adminService.createNewVersion(id, updated);
    }

    @PatchMapping("/{id}/rollback")
    public void rollback(@PathVariable Long id) {

        BugHunterQuestion current = repo.findById(id).orElseThrow();

        BugHunterQuestion previous =
                repo.findTopByParentIdOrderByVersionDesc(
                        current.getParentId()
                ).orElseThrow();

        previous.setPublished(true);
        current.setPublished(false);

        repo.save(previous);
        repo.save(current);
    }

    @GetMapping("/analytics/questions")
    public List<BugQuestionAnalytics> questionAnalytics() {
        return repo.analytics();
    }

    @GetMapping("/analytics/categories")
    public List<BugCategoryAnalytics> categoryAnalytics() {
        return attemptRepo.categoryAnalytics();
    }

    @GetMapping("/analytics/questions/accuracy")
    public List<BugQuestionAccuracy> questionAccuracy() {
        return attemptRepo.questionAccuracy();
    }

}
