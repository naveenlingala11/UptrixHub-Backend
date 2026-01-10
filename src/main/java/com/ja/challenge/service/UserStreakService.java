package com.ja.challenge.service;

import com.ja.challenge.dto.UserStreakDto;
import com.ja.challenge.repository.UserChallengeProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStreakService {

    private final UserChallengeProgressRepository repo;

    public UserStreakDto calculate(Long userId) {

        List<String> dates = repo.findSolvedDates(userId);

        int current = 0;
        int longest = 0;

        LocalDate prev = null;

        for (String d : dates) {
            LocalDate date = LocalDate.parse(d);

            if (prev == null) {
                current = 1;
            } else if (prev.minusDays(1).equals(date)) {
                current++;
            } else {
                longest = Math.max(longest, current);
                current = 1;
            }
            prev = date;
        }

        longest = Math.max(longest, current);

        int xp = repo.totalXp(userId);

        return UserStreakDto.builder()
                .currentStreak(current)
                .longestStreak(longest)
                .streak7(current >= 7)
                .streak14(current >= 14)
                .streak30(current >= 30)
                .totalXp(xp)
                .build();
    }
}
