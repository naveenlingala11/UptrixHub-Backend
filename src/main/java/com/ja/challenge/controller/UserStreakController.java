package com.ja.challenge.controller;

import com.ja.challenge.dto.UserStreakDto;
import com.ja.challenge.service.UserStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streak")
@CrossOrigin
@RequiredArgsConstructor
public class UserStreakController {

    private final UserStreakService service;

    @GetMapping("/{userId}")
    public UserStreakDto get(@PathVariable Long userId) {
        return service.calculate(userId);
    }
}
