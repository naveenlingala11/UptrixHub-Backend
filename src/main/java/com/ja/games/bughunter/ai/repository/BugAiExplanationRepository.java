package com.ja.games.bughunter.ai.repository;

import com.ja.games.bughunter.ai.entity.BugAiExplanation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BugAiExplanationRepository
        extends JpaRepository<BugAiExplanation, Long> {

    Optional<BugAiExplanation>
    findTopByQuestionIdAndBugTypeAndDifficulty(
            Long questionId,
            String bugType,
            String difficulty
    );
}
