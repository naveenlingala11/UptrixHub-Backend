package com.ja.home.kit.service;

import com.ja.home.kit.dto.UserKitDto;
import com.ja.home.kit.repository.UserKitProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserKitService {

    private final UserKitProgressRepository repo;

    public List<UserKitDto> getUserKits(Long userId) {

        return repo.findByUserId(userId)
                .stream()
                .map(k -> new UserKitDto(
                        k.getKitCode(),
                        mapTitle(k.getKitCode()),
                        k.getProgress(),
                        k.isActive(),
                        k.isPurchased(),
                        mapRoute(k.getKitCode())
                ))
                .toList();
    }

    private String mapTitle(String code) {
        return switch (code) {
            case "CORE_JAVA" -> "Core Java Interview Kit";
            case "SPRING_BOOT" -> "Spring Boot Interview Kit";
            case "MICROSERVICES" -> "Microservices Interview Kit";
            default -> "Unknown Kit";
        };
    }

    private String mapRoute(String code) {
        return switch (code) {
            case "CORE_JAVA" -> "/kits/core-java";
            case "SPRING_BOOT" -> "/kits/spring-boot";
            case "MICROSERVICES" -> "/kits/microservices";
            default -> "/";
        };
    }
}
