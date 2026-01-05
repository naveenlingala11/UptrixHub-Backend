package com.ja.features.mock.interview.controller;

import com.ja.features.mock.interview.dto.MockInterviewRequestDto;
import com.ja.features.mock.interview.dto.MockInterviewSessionDto;
import com.ja.features.mock.interview.service.MockInterviewService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockInterviewUserController {

    private final MockInterviewService service;

    @PostMapping("/request")
    public void request(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestBody MockInterviewRequestDto dto
    ) {
        service.requestMock(user.getUserId(), dto);
    }

    @GetMapping("/my-requests")
    public List<MockInterviewRequestDto> myRequests(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        return service.myRequests(user.getUserId());
    }

    @GetMapping("/my-sessions")
    public List<MockInterviewSessionDto> mySessions(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        return service.mySessions(user.getUserId());
    }

}
