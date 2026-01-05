package com.ja.games.bughunter.service;

import com.ja.games.bughunter.dto.BugHunterAnswerRequest;
import com.ja.games.bughunter.dto.BugHunterAnswerResponse;
import com.ja.games.bughunter.dto.BugHunterHistoryResponse;
import com.ja.games.bughunter.entity.BugHunterAttempt;
import com.ja.games.bughunter.entity.BugHunterQuestion;
import com.ja.games.bughunter.repository.BugHunterAttemptRepository;
import com.ja.games.bughunter.repository.BugHunterQuestionRepository;
import com.ja.games.xp.service.XpEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BugHunterService {

    private final BugHunterQuestionRepository questionRepo;
    private final BugHunterAttemptRepository attemptRepo;
    private final XpEngineService xpService;

    /* ================= QUESTION ================= */

    public BugHunterQuestion getRandomQuestion(String language) {

        List<BugHunterQuestion> list =
                questionRepo.findByPublishedTrueAndActiveTrue()
                        .stream()
                        .filter(q -> q.getLanguage().equals(language))
                        .toList();

        if (list.isEmpty()) {
            throw new RuntimeException("No published Bug Hunter questions");
        }

        return list.get(new Random().nextInt(list.size()));
    }

    /* ================= SUBMIT ANSWER ================= */

    public BugHunterAnswerResponse submitAnswer(BugHunterAnswerRequest req) {

        BugHunterQuestion q = questionRepo.findById(req.questionId())
                .orElseThrow();

        boolean correct =
                q.getBugType().equalsIgnoreCase(req.selectedAnswer());

        int earnedXp = correct ? q.getXp() : 0;

        // ✅ SAVE ATTEMPT (THIS WAS MISSING)
        BugHunterAttempt attempt = new BugHunterAttempt();
        attempt.setUserId(req.userId());
        attempt.setQuestionId(q.getId());
        attempt.setSelectedAnswer(req.selectedAnswer());
        attempt.setCorrect(correct);
        attempt.setBugCategory(q.getBugCategory());
        attempt.setEarnedXp(earnedXp);

        attemptRepo.save(attempt);

        // ✅ XP ENGINE (ONLY IF CORRECT)
        if (correct) {
            xpService.addXp(
                    req.userId(),
                    earnedXp,
                    "BUG_HUNTER",
                    q.getId(),
                    "CORRECT_ANSWER"
            );
        }

        return new BugHunterAnswerResponse(correct, earnedXp);
    }

    /* ================= HISTORY ================= */

    public List<BugHunterAttempt> getHistory(Long userId) {
        return attemptRepo.findByUserIdOrderByAttemptedAtDesc(userId);
    }

    public List<BugHunterHistoryResponse> getHistoryDto(Long userId) {

        return attemptRepo.findByUserIdOrderByAttemptedAtDesc(userId)
                .stream()
                .map(a -> new BugHunterHistoryResponse(
                        a.getQuestionId(),
                        a.getBugCategory(),
                        a.getSelectedAnswer(),
                        a.isCorrect(),
                        a.getEarnedXp(),
                        a.getAttemptedAt()
                ))
                .toList();
    }
}
