package com.ja.course.controller;

import com.ja.course.dto.CourseCardDto;
import com.ja.course.dto.CourseDetailResponse;
import com.ja.course.entity.Course;
import com.ja.course.player.CoursePlayerData;
import com.ja.course.repository.CourseRepository;
import com.ja.course.service.CourseAccessService;
import com.ja.course.service.CourseQueryService;
import com.ja.course.service.CourseService;
import com.ja.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/public/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepo;
    private final CourseAccessService courseAccessService;
    private final CourseQueryService queryService;
    private final CourseService courseService;
    private final ObjectMapper mapper;

    /* ================= COURSE LIST ================= */
    @GetMapping
    public List<CourseCardDto> listCourses() {
        return queryService.getAllCourses();
    }

    /* ================= COURSE DETAIL ================= */
    @GetMapping("/{courseId}")
    public CourseDetailResponse getCourse(
            @PathVariable String courseId,
            @AuthenticationPrincipal User user
    ) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        boolean unlocked =
                user != null &&
                        courseAccessService.canAccess(user.getId(), course);

        return CourseDetailResponse.from(course, unlocked);
    }

    /* ================= COURSE CONTENT ================= */
    @GetMapping("/{id}/content")
    public ResponseEntity<?> getCourseContent(
            @PathVariable String id,
            @AuthenticationPrincipal User user
    ) {
        if (user == null ||
                !courseAccessService.hasAccess(user.getId(), id)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Purchase required");
        }

        return ResponseEntity.ok(courseService.getCourseContent(id));
    }

    /* ================= INTERNAL (OPTIONAL) ================= */
    @SuppressWarnings("unused")
    private CoursePlayerData getPublishedContent(String courseId) {

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.isPublished()) {
            throw new RuntimeException("Course not published");
        }

        return mapper.convertValue(
                course.getPublishedContent(),
                CoursePlayerData.class
        );
    }
}
