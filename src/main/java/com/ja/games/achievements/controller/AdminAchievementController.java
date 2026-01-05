package com.ja.games.achievements.controller;

import com.ja.games.entity.Achievement;
import com.ja.games.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/achievements")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAchievementController {

    private final AchievementRepository repo;

    @GetMapping
    public List<Achievement> all() {
        return repo.findAll();
    }

    @PostMapping
    public Achievement create(@RequestBody Achievement a) {
        return repo.save(a);
    }

    @PutMapping("/{id}")
    public Achievement update(
            @PathVariable Long id,
            @RequestBody Achievement a) {

        Achievement ex = repo.findById(id).orElseThrow();
        ex.setTitle(a.getTitle());
        ex.setDescription(a.getDescription());
        ex.setIcon(a.getIcon());
        ex.setRequiredXp(a.getRequiredXp());
        ex.setRequiredStreak(a.getRequiredStreak());
        ex.setEnabled(a.isEnabled());
        return repo.save(ex);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
