package com.ja.admin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.course.entity.Course;
import com.ja.course.entity.CourseVersion;
import com.ja.course.player.CoursePlayerData;
import com.ja.course.repository.CourseRepository;
import com.ja.course.repository.CourseVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCourseContentController {

    private final CourseRepository courseRepository;
    private final CourseVersionRepository versionRepository;
    private final ObjectMapper objectMapper;

    /* ================= UPLOAD DRAFT ================= */

    @PostMapping("/{courseId}/content/draft")
    public ResponseEntity<?> uploadDraft(
            @PathVariable String courseId,
            @RequestBody CoursePlayerData data
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        JsonNode json = objectMapper.valueToTree(data);

        course.setDraftContent(json);
        course.setPublished(false);

        courseRepository.save(course);

        return ResponseEntity.ok(Map.of("message", "Draft saved"));
    }

    /* ================= PREVIEW ================= */

    @GetMapping("/{courseId}/preview")
    public CoursePlayerData preview(@PathVariable String courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow();

        if (course.getDraftContent() == null) {
            throw new RuntimeException("No draft uploaded");
        }

        return objectMapper.convertValue(
                course.getDraftContent(),
                CoursePlayerData.class
        );
    }

    /* ================= PUBLISH ================= */

    @PostMapping("/{courseId}/publish")
    public ResponseEntity<?> publish(@PathVariable String courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow();

        if (course.getDraftContent() == null) {
            throw new RuntimeException("No draft to publish");
        }

        int nextVersion = course.getContentVersion() == null
                ? 1
                : course.getContentVersion() + 1;

        // 🔥 Save version snapshot
        CourseVersion version = new CourseVersion();
        version.setCourseId(courseId);
        version.setVersion(nextVersion);
        version.setContent(course.getDraftContent());

        versionRepository.save(version);

        course.setPublishedContent(course.getDraftContent());
        course.setContentVersion(nextVersion);
        course.setPublished(true);

        courseRepository.save(course);

        return ResponseEntity.ok(Map.of(
                "message", "Published",
                "version", nextVersion
        ));
    }

    /* ================= UNPUBLISH ================= */

    @PostMapping("/{courseId}/unpublish")
    public ResponseEntity<?> unpublish(@PathVariable String courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow();

        course.setPublished(false);
        courseRepository.save(course);

        return ResponseEntity.ok(Map.of("message", "Moved to draft"));
    }

    /* ================= VERSION LIST ================= */

    @GetMapping("/{courseId}/versions")
    public List<CourseVersion> versions(@PathVariable String courseId) {
        return versionRepository.findByCourseIdOrderByVersionDesc(courseId);
    }

    @GetMapping("/{courseId}/versions/{version}")
    public CoursePlayerData getVersion(
            @PathVariable String courseId,
            @PathVariable Integer version
    ) {
        CourseVersion v = versionRepository
                .findByCourseIdAndVersion(courseId, version)
                .orElseThrow();

        return objectMapper.convertValue(
                v.getContent(),
                CoursePlayerData.class
        );
    }

    /* ================= ROLLBACK ================= */

    @PostMapping("/{courseId}/rollback/{version}")
    public ResponseEntity<?> rollback(
            @PathVariable String courseId,
            @PathVariable Integer version
    ) {
        CourseVersion v = versionRepository
                .findByCourseIdAndVersion(courseId, version)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow();

        course.setDraftContent(v.getContent());
        course.setPublished(false);

        courseRepository.save(course);

        return ResponseEntity.ok(
                Map.of("message", "Rolled back to v" + version)
        );
    }
}
