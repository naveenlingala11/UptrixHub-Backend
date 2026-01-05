package com.ja.home.kit.controller;

import com.ja.home.kit.dto.UserKitDto;
import com.ja.home.kit.service.UserKitService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class UserKitController {

    private final UserKitService service;

    @GetMapping("/kits")
    public List<UserKitDto> myKits(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        System.out.println("USER ID = " + principal.getUserId());
        return service.getUserKits(principal.getUserId());
    }
}

