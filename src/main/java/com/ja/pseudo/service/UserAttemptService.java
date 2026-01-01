package com.ja.pseudo.service;

import com.ja.pseudo.dto.UserAttemptResponse;
import com.ja.pseudo.entity.PseudoTestAttempt;
import com.ja.pseudo.repository.PseudoTestAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAttemptService {

    private final PseudoTestAttemptRepository attemptRepo;

    public Page<UserAttemptResponse> getUserAttempts(
            Long userId,
            String skill,
            int page,
            int size
    ) {

        Page<PseudoTestAttempt> attempts;

        Pageable pageable = PageRequest.of(page, size);

        if (skill == null || skill.isBlank()) {
            attempts = attemptRepo
                    .findByUserIdOrderByCreatedAtDesc(userId, pageable);
        } else {
            attempts = attemptRepo
                    .findByUserIdAndSkill_SlugOrderByCreatedAtDesc(
                            userId, skill, pageable);
        }

        return attempts.map(UserAttemptResponse::from);
    }
}

