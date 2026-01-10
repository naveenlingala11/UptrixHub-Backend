package com.ja.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.challenge.dto.AdminDailyChallengeRequest;
import com.ja.challenge.dto.TestCaseDto;
import com.ja.challenge.entity.DailyChallenge;
import com.ja.challenge.repository.DailyChallengeRepository;
import com.ja.challenge.repository.UserChallengeProgressRepository;
import com.ja.config.redis.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDailyChallengeService {

    private final DailyChallengeRepository repo;
    private final UserChallengeProgressRepository progressRepo;
    private final ObjectMapper mapper;
    private final RedisLockService redisLockService;

    @Transactional
    @CacheEvict(value = "daily-challenge", allEntries = true)
    public void upsert(MultipartFile file, String json) {

        String lockKey = null;

        try {
            String payload = file != null
                    ? new String(file.getBytes())
                    : json;

            AdminDailyChallengeRequest req =
                    mapper.readValue(payload, AdminDailyChallengeRequest.class);

            validate(req);

            if (progressRepo.countByChallengeDate(req.getDate()) > 0) {
                throw new RuntimeException("Challenge already attempted");
            }

            lockKey = "lock:daily:" + req.getDate();
            if (!redisLockService.lock(lockKey, 30)) {
                throw new RuntimeException("Another admin is editing");
            }

            DailyChallenge challenge =
                    repo.findByChallengeDate(req.getDate())
                            .orElse(new DailyChallenge());

            challenge.setChallengeDate(req.getDate());
            challenge.setTitle(req.getTitle());
            challenge.setDifficulty(req.getDifficulty());
            challenge.setTime(req.getTime());
            challenge.setTags(new HashSet<>(req.getTags()));
            challenge.setProblem(req.getProblem());
            challenge.setStarterCode(req.getStarterCode());
            challenge.setXpReward(req.getXpReward());
            challenge.setTests(req.getTests());

            repo.save(challenge);

        } catch (Exception e) {
            log.error("Daily challenge upload failed", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (lockKey != null) {
                redisLockService.unlock(lockKey);
            }
        }
    }

    private List<com.ja.code.dto.TestCase> toRunnerTests(
            List<TestCaseDto> tests
    ) {
        return tests.stream()
                .map(t -> new com.ja.code.dto.TestCase(
                        t.getInput(),
                        t.getExpectedOutput()
                ))
                .toList();
    }

    private void validate(AdminDailyChallengeRequest r) {
        if (r.getDate() == null) throw new RuntimeException("date required");
        if (r.getTitle() == null) throw new RuntimeException("title required");
        if (r.getTests() == null || r.getTests().isEmpty())
            throw new RuntimeException("tests required");
    }
}

