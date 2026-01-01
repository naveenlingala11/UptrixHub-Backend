package com.ja.pseudo.controller;

import com.ja.pseudo.entity.PseudoSkill;
import com.ja.pseudo.repository.PseudoSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pseudo-code")
@RequiredArgsConstructor
public class PseudoSkillController {

    private final PseudoSkillRepository skillRepository;

    @GetMapping("/skills")
    public List<PseudoSkill> getSkills() {
        return skillRepository.findByActiveTrue();
    }
}
