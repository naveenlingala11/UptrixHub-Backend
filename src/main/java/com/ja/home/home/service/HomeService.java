package com.ja.home.home.service;

import com.ja.home.home.dto.*;
import com.ja.user.repository.UserDailyActivityRepository;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepo;
    private final UserDailyActivityRepository dailyRepo;

    public DashboardDto getDashboard(Long userId) {

        int solved = dailyRepo
                .findByUser_IdAndDateAfter(userId,
                        LocalDate.now().minusDays(30))
                .stream()
                .mapToInt(a -> a.getCount())
                .sum();

        return new DashboardDto(
                3,
                Math.min(solved, 100),
                solved,
                7
        );
    }

    public List<LearningPathDto> getLearningPath(Long userId) {
        return List.of(
                new LearningPathDto(
                        "Finish Java Collections",
                        "List, Set, Map internals",
                        72,
                        "Continue",
                        "/dashboard/home",
                        "in-progress"
                ),
                new LearningPathDto(
                        "Start Spring Boot REST APIs",
                        "Controllers, JPA, REST",
                        0,
                        "Start",
                        "/roadmaps",
                        "recommended"
                )
        );
    }

    public List<ActivityHeatmapDto> getHeatmap(Long userId) {
        return dailyRepo
                .findByUser_IdAndDateAfter(userId,
                        LocalDate.now().minusDays(90))
                .stream()
                .map(a -> new ActivityHeatmapDto(
                        a.getDate(),
                        a.getCount()
                ))
                .toList();
    }

    public MockInterviewProgressDto getMockProgress(Long userId) {

        // TEMP DUMMY DATA (later connect mock-interview module)
        return new MockInterviewProgressDto(
                72,                 // readiness score
                3,                  // attempted mocks
                5,                  // total mocks
                LocalDateTime.now().plusDays(3) // next scheduled mock
        );
    }

    public SalaryInsightDto getSalaryInsight(Long userId) {
        return new SalaryInsightDto(
                6,
                12,
                78,
                List.of(
                        new SalaryInsightDto.RoleRange(
                                "Java Backend Developer",
                                "10 – 14 LPA"
                        ),
                        new SalaryInsightDto.RoleRange(
                                "Spring Boot Engineer",
                                "12 – 16 LPA"
                        )
                ),
                List.of(
                        "Improve system design answers",
                        "Practice mock interviews"
                )
        );
    }
}
