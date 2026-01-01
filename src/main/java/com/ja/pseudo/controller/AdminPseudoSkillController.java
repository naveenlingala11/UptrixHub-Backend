package com.ja.pseudo.controller;

import com.ja.pseudo.entity.PseudoSkill;
import com.ja.pseudo.repository.PseudoSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pseudo")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPseudoSkillController {

    private final PseudoSkillRepository skillRepo;

    @GetMapping("/skills")
    public List<PseudoSkill> getSkills() {
        return skillRepo.findByActiveTrueOrderByTitleAsc();
    }
}
