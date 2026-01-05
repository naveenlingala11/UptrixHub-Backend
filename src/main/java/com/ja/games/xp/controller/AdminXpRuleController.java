package com.ja.games.xp.controller;

import com.ja.games.entity.XpRule;
import com.ja.games.repository.XpRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/xp-rules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminXpRuleController {

    private final XpRuleRepository repo;

    @GetMapping
    public List<XpRule> all() {
        return repo.findAll();
    }

    @PostMapping
    public XpRule create(@RequestBody XpRule r) {
        validate(r);
        return repo.save(r);
    }

    @PutMapping("/{id}")
    public XpRule update(
            @PathVariable Long id,
            @RequestBody XpRule r) {

        validate(r);

        XpRule ex = repo.findById(id).orElseThrow();
        ex.setGameType(r.getGameType());
        ex.setAction(r.getAction());
        ex.setXp(r.getXp());
        ex.setEnabled(r.isEnabled());

        return repo.save(ex);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }

    private void validate(XpRule r) {
        if (r.getXp() == null || r.getXp() < 0 || r.getXp() > 1000) {
            throw new IllegalArgumentException("XP must be 0–1000");
        }
    }
}
