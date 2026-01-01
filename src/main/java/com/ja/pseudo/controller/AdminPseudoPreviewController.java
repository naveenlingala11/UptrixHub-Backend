package com.ja.pseudo.controller;

import com.ja.pseudo.entity.PseudoQuestion;
import com.ja.pseudo.entity.PseudoSkill;
import com.ja.pseudo.repository.PseudoQuestionRepository;
import com.ja.pseudo.repository.PseudoSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pseudo")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPseudoPreviewController {

    private final PseudoSkillRepository skillRepo;
    private final PseudoQuestionRepository questionRepo;

    @GetMapping("/questions/{skill}")
    public List<PseudoQuestion> preview(@PathVariable String skill) {

        PseudoSkill s = skillRepo.findBySlugAndActiveTrue(skill)
                .orElseThrow();

        return questionRepo.findAllBySkillOrderByIdAsc(s);
    }
}
