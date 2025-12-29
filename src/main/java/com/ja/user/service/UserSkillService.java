package com.ja.user.service;

import com.ja.skill.SkillEndorsement;
import com.ja.skill.SkillEndorsementRepository;
import com.ja.user.dto.AddSkillRequest;
import com.ja.user.entity.User;
import com.ja.user.entity.UserSkill;
import com.ja.user.repository.UserRepository;
import com.ja.user.repository.UserSkillRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class UserSkillService {

    private final UserRepository userRepo;
    private final UserSkillRepository skillRepo;
    private final SkillEndorsementRepository endorsementRepo;

    public void addSkill(Long userId, AddSkillRequest req) {

        if (req.getName() == null || req.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Skill name required"
            );
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        UserSkill skill = new UserSkill();
        skill.setName(req.getName());
        skill.setProficiency(req.getProficiency());
        skill.setUser(user);

        skillRepo.save(skill);
    }

    @Transactional
    public void endorseSkill(Long endorserId, Long skillId) {

        if (endorsementRepo.existsByEndorserIdAndSkillId(endorserId, skillId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Already endorsed"
            );
        }

        User endorser = userRepo.findById(endorserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Endorser not found"
                ));

        UserSkill skill = skillRepo.findById(skillId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Skill not found"
                ));

        endorsementRepo.save(
                new SkillEndorsement(endorser, skill)
        );
    }
}

