package com.ja.pseudo.service;

import com.ja.pseudo.config.TestConfig;
import com.ja.pseudo.dto.ResumeTestResponse;
import com.ja.pseudo.dto.StartTestResponse;
import com.ja.pseudo.dto.TestSubmitRequest;
import com.ja.pseudo.dto.TestSubmitResponse;
import com.ja.pseudo.entity.*;
import com.ja.pseudo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PseudoTestService {

    private final PseudoSkillRepository skillRepo;
    private final PseudoQuestionRepository questionRepo;
    private final PseudoTestAttemptRepository attemptRepo;
    private final PseudoTestAnswerRepository answerRepo;
    private final PseudoTestViolationRepository violationRepo;

    public StartTestResponse startTest(String skillSlug, Long userId) {

        PseudoSkill skill = skillRepo
                .findBySlugAndActiveTrue(skillSlug)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(TestConfig.TEST_DURATION_MINUTES);

        PseudoTestAttempt attempt = new PseudoTestAttempt();
        attempt.setSkill(skill);
        attempt.setUserId(userId);
        attempt.setStartedAt(now);
        attempt.setExpiresAt(expires);
        attempt.setCompleted(false);

        attemptRepo.save(attempt);

        return new StartTestResponse(
                attempt.getId(),
                now,
                expires,
                TestConfig.TEST_DURATION_MINUTES
        );
    }

    public TestSubmitResponse submitTest(
            Long attemptId,
            TestSubmitRequest request,
            Long userId
    ) {

        PseudoTestAttempt attempt =
                attemptRepo.findByIdAndUserId(attemptId, userId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Attempt not found for user"));

        if (attempt.isCompleted()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Test already submitted");
        }

        if (LocalDateTime.now().isAfter(attempt.getExpiresAt())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Test time expired");
        }

        int correct = 0;
        int wrong = 0;

        for (Map.Entry<Long, Character> entry : request.getAnswers().entrySet()) {
            PseudoQuestion q = questionRepo.findById(entry.getKey())
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Question not found"));

            if (Character.toUpperCase(entry.getValue())
                    == Character.toUpperCase(q.getCorrectOption())) {
                correct++;
            } else {
                wrong++;
            }
        }

        int total = correct + wrong;
        int score = correct * 2;

        attempt.setTotalQuestions(total);
        attempt.setCorrectAnswers(correct);
        attempt.setWrongAnswers(wrong);
        attempt.setScore(score);
        attempt.setCompleted(true);

        attemptRepo.save(attempt);

        return new TestSubmitResponse(total, correct, wrong, score);
    }


    public ResumeTestResponse resumeTest(String skill, Long userId) {

        PseudoTestAttempt attempt =
                attemptRepo.findTopByUserIdAndSkill_SlugAndCompletedFalseOrderByStartedAtDesc(
                                userId, skill)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (LocalDateTime.now().isAfter(attempt.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.GONE, "Expired");
        }

        List<PseudoTestAnswer> saved =
                answerRepo.findByAttemptId(attempt.getId());

        Map<Long, Character> answers = saved.stream()
                .collect(Collectors.toMap(
                        PseudoTestAnswer::getQuestionId,
                        PseudoTestAnswer::getSelectedOption
                ));

        return new ResumeTestResponse(
                attempt.getId(),
                attempt.getExpiresAt(),
                answers
        );
    }

    private void autoSubmit(Long attemptId) {

        PseudoTestAttempt attempt = attemptRepo.findById(attemptId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Attempt not found"));

        if (attempt.isCompleted()) {
            return; // already submitted
        }

        List<PseudoTestAnswer> answers =
                answerRepo.findByAttemptId(attemptId);

        int correct = 0;
        int wrong = 0;

        for (PseudoTestAnswer a : answers) {
            PseudoQuestion q = questionRepo.findById(a.getQuestionId())
                    .orElseThrow();

            if (a.getSelectedOption() != null &&
                    Character.toUpperCase(a.getSelectedOption())
                            == Character.toUpperCase(q.getCorrectOption())) {
                correct++;
            } else {
                wrong++;
            }
        }

        int total = correct + wrong;
        int score = correct * 2;

        attempt.setTotalQuestions(total);
        attempt.setCorrectAnswers(correct);
        attempt.setWrongAnswers(wrong);
        attempt.setScore(score);
        attempt.setCompleted(true);

        attemptRepo.save(attempt);
    }

    public long logViolation(Long attemptId, String type) {

        PseudoTestViolation v = new PseudoTestViolation();
        v.setAttemptId(attemptId);
        v.setType(type);

        violationRepo.save(v);

        long count = violationRepo.countByAttemptId(attemptId);

        if (count >= 3) {
            autoSubmit(attemptId); // same logic as timer
        }

        return count;
    }

}

