package com.ja.games.bughunter.controller;

import com.ja.games.bughunter.dto.BugHunterHistoryResponse;
import com.ja.games.bughunter.service.BugHunterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bug-hunter")
@RequiredArgsConstructor
public class BugHunterHistoryController {

    private final BugHunterService service;

    @GetMapping("/history/{userId}")
    public List<BugHunterHistoryResponse> history(
            @PathVariable Long userId
    ) {
        return service.getHistoryDto(userId);
    }
}
