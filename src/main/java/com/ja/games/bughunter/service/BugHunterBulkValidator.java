package com.ja.games.bughunter.service;

import com.ja.games.bughunter.dto.BugHunterQuestionRequest;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BugHunterBulkValidator {

    public Map<String, Object> validate(List<BugHunterQuestionRequest> list) {

        List<String> errors = new ArrayList<>();
        Set<String> uniqueCheck = new HashSet<>();

        int index = 0;
        for (BugHunterQuestionRequest q : list) {

            if (q.getCode() == null || q.getCode().isBlank())
                errors.add("Question " + index + ": code missing");

            if (q.getBugType() == null)
                errors.add("Question " + index + ": bugType missing");

            if (q.getXp() <= 0)
                errors.add("Question " + index + ": XP must be > 0");

            String hash =
                    q.getLanguage() + "|" +
                            q.getTitle() + "|" +
                            q.getBugType();

            if (!uniqueCheck.add(hash)) {
                errors.add("Duplicate detected at index " + index);
            }

            index++;
        }

        return Map.of(
                "total", list.size(),
                "valid", errors.isEmpty(),
                "errors", errors
        );
    }
}
