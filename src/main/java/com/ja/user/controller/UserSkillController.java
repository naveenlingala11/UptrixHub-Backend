package com.ja.user.controller;

import com.ja.security.JwtUserPrincipal;
import com.ja.user.dto.AddSkillRequest;
import com.ja.user.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/skills")
@RequiredArgsConstructor
public class UserSkillController {

    private final UserSkillService skillService;

    @PostMapping
    public void addSkill(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody AddSkillRequest req
    ) {
        skillService.addSkill(principal.getUserId(), req);
    }

    @PostMapping("/{skillId}/endorse")
    public void endorseSkill(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long skillId
    ) {
        skillService.endorseSkill(principal.getUserId(), skillId);
    }
}
