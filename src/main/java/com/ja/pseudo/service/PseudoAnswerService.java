package com.ja.pseudo.service;

import com.ja.pseudo.dto.AnswerSubmitRequest;
import com.ja.pseudo.dto.AnswerSubmitResponse;
import com.ja.pseudo.entity.PseudoAnswerLog;
import com.ja.pseudo.entity.PseudoQuestion;
import com.ja.pseudo.entity.PseudoTestAnswer;
import com.ja.pseudo.repository.PseudoAnswerLogRepository;
import com.ja.pseudo.repository.PseudoQuestionRepository;
import com.ja.pseudo.repository.PseudoTestAnswerRepository;
import com.ja.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PseudoAnswerService {

    private final PseudoQuestionRepository questionRepo;
    private final PseudoAnswerLogRepository logRepo;
    private final PseudoTestAnswerRepository answerRepo;
    private final JsonUtil jsonUtil;

    public AnswerSubmitResponse checkAnswer(AnswerSubmitRequest req) {

        PseudoQuestion q = questionRepo.findById(req.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Question not found"));

        boolean isCorrect =
                Character.toUpperCase(req.getSelectedOption())
                        == Character.toUpperCase(q.getCorrectOption());

        PseudoAnswerLog log = new PseudoAnswerLog();
        log.setQuestionId(q.getId());
        log.setCorrect(isCorrect);
        logRepo.save(log);

        return new AnswerSubmitResponse(
                isCorrect,
                q.getExplanationSummary(),
                jsonUtil.readList(q.getExplanationStepsJson()),
                jsonUtil.readList(q.getExplanationConceptsJson())
        );

    }

    public void saveAnswer(Long attemptId, AnswerSubmitRequest req) {

        PseudoTestAnswer ans =
                answerRepo.findByAttemptIdAndQuestionId(
                                attemptId, req.getQuestionId())
                        .orElse(new PseudoTestAnswer());

        ans.setAttemptId(attemptId);
        ans.setQuestionId(req.getQuestionId());
        ans.setSelectedOption(req.getSelectedOption());

        answerRepo.save(ans);
    }
}



