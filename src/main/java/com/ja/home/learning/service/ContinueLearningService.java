package com.ja.home.learning.service;

import com.ja.home.learning.dto.ContinueLearningDto;
import com.ja.home.learning.entity.UserContinueLearning;
import com.ja.home.learning.repository.UserContinueLearningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContinueLearningService {

    private final UserContinueLearningRepository repo;

    public ContinueLearningDto get(Long userId) {

        var list = repo.findAllByUserId(userId);

        if (list.isEmpty()) {
            throw new RuntimeException("NO CONTINUE LEARNING DATA");
        }

        // ✅ Priority logic
        UserContinueLearning entity = list.stream()
                .filter(e -> "SPRING_BOOT".equals(e.getKitCode()))
                .findFirst()
                .orElse(list.get(0));

        return new ContinueLearningDto(
                mapKit(entity.getKitCode()),
                entity.getLastTopic(),
                entity.getResumeRoute(),
                entity.getStreakDays()
        );
    }

    private String mapKit(String code) {
        return switch (code) {
            case "CORE_JAVA" -> "Core Java Interview Kit";
            case "SPRING_BOOT" -> "Spring Boot Interview Kit";
            case "MICROSERVICES" -> "Microservices Interview Kit";
            default -> "Interview Kit";
        };
    }
}

