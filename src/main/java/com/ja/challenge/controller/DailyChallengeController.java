package com.ja.challenge.controller;

import com.ja.challenge.dto.DailyChallengeResponse;
import com.ja.challenge.dto.DailyChallengeView;
import com.ja.challenge.dto.SubmitChallengeRequest;
import com.ja.challenge.service.DailyChallengeQueryService;
import com.ja.challenge.service.DailyChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/daily-challenge")
@RequiredArgsConstructor
public class DailyChallengeController {

    private final DailyChallengeQueryService queryService;
    private final DailyChallengeService submitService;

    @GetMapping("/today")
    public DailyChallengeView today(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return queryService.getToday(userId);
    }

    @PostMapping("/submit")
    public DailyChallengeResponse submit(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody SubmitChallengeRequest req
    ) throws Exception {
        return submitService.submit(userId, req);
    }
}
