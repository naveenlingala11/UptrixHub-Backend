package com.ja.course.controller;

import com.ja.course.entity.Course;
import com.ja.course.enums.PriceType;
import com.ja.course.repository.CourseRepository;
import com.ja.course.service.CourseAccessService;
import com.ja.course.service.CourseService;
import com.ja.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseContentController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final CourseAccessService courseAccessService;

    @GetMapping(
            value = "/{courseId}/content",
            produces = MediaType.APPLICATION_JSON_VALUE   // 🔥 IMPORTANT FIX
    )
    public ResponseEntity<?> getCourseContent(
            @PathVariable String courseId,
            @AuthenticationPrincipal User user
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // ❌ Not published
        if (!course.isPublished()) {
            return ResponseEntity.status(403).body("Not published");
        }

        // 🆓 FREE COURSE → ALWAYS ALLOW
        if (course.getPriceType() == PriceType.FREE) {
            return ResponseEntity.ok(
                    courseService.getCourseContent(courseId)
            );
        }

        // 🔒 PAID COURSE → ACCESS CHECK
        if (user == null ||
                !courseAccessService.canAccess(user.getId(), course)) {
            return ResponseEntity.status(403)
                    .body("Purchase required");
        }

        return ResponseEntity.ok(
                courseService.getCourseContent(courseId)
        );
    }

}


