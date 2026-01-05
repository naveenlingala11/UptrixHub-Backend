package com.ja.games.bughunter.repository;

import com.ja.games.bughunter.entity.BugGameLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BugGameLevelRepository
        extends JpaRepository<BugGameLevel, Long> {

    Optional<BugGameLevel> findByIdAndActiveTrue(Long id);
}
