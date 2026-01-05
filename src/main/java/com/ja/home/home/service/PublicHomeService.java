package com.ja.home.home.service;

import com.ja.home.home.dto.PublicStatsDto;
import com.ja.user.enums.Subscription;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicHomeService {

    private final UserRepository userRepo;

    public PublicStatsDto getStats() {

        long total = userRepo.count();
        long pro = userRepo.countBySubscriptionAndDeletedFalse(Subscription.PRO);

        int successRate = total == 0 ? 0 :
                (int) ((pro * 100) / total);

        return new PublicStatsDto(
                (int) total,
                successRate,
                4.9
        );
    }
}
