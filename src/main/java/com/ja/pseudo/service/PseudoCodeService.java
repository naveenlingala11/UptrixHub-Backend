package com.ja.pseudo.service;

import com.ja.pseudo.entity.PseudoQuestion;
import com.ja.pseudo.entity.PseudoSkill;
import com.ja.pseudo.repository.PseudoQuestionRepository;
import com.ja.pseudo.repository.PseudoSkillRepository;
import com.ja.user.entity.User;
import com.ja.user.enums.Subscription;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PseudoCodeService {

    private final PseudoSkillRepository skillRepo;
    private final PseudoQuestionRepository questionRepo;
    private final UserRepository userRepo;

    public Page<PseudoQuestion> getQuestions(
            String skillSlug,
            int page,
            int size,
            Long userId
    ) {
        PseudoSkill skill = skillRepo
                .findBySlugAndActiveTrue(skillSlug)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Skill not found"));

        boolean isPro = false;

        if (userId != null) {
            User user = userRepo.findById(userId).orElseThrow();
            isPro = user.getSubscription() == Subscription.PRO;
        }

        return questionRepo.findBySkillWithAccess(
                skill,
                isPro,
                PageRequest.of(page, size)
        );
    }
}
