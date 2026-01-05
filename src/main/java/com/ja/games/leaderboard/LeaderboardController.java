package com.ja.games.leaderboard;

import com.ja.games.entity.UserXp;
import com.ja.games.repository.UserXpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final UserXpRepository repo;

    @GetMapping("/top")
    public List<UserXp> topPlayers() {
        return repo.findAll(
                PageRequest.of(
                        0,
                        20,
                        Sort.by("totalXp").descending()
                )
        ).getContent();
    }
}
