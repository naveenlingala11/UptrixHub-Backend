package com.ja.home.home.controller;

import com.ja.home.home.dto.PublicStatsDto;
import com.ja.home.home.service.PublicHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final PublicHomeService publicHomeService;

    @GetMapping("/stats")
    public PublicStatsDto stats() {
        return publicHomeService.getStats();
    }
}
