package com.ja.features.mock.interview.controller;

import com.ja.features.mock.interview.dto.ReviewMockDto;
import com.ja.features.mock.interview.dto.ScheduleMockDto;
import com.ja.features.mock.interview.service.MockInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/mock")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MockInterviewAdminController {

    private final MockInterviewService service;

    @GetMapping("/requests")
    public Object pending() {
        return service.pendingRequests();
    }

    @PostMapping("/schedule")
    public void schedule(@RequestBody ScheduleMockDto dto) {
        service.schedule(dto);
    }

    @PostMapping("/review/{sessionId}")
    public void review(
            @PathVariable Long sessionId,
            @RequestBody ReviewMockDto dto
    ) {
        service.review(sessionId, dto);
    }
}
