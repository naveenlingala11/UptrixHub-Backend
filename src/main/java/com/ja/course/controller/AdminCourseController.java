package com.ja.course.controller;

import com.ja.course.entity.Course;
import com.ja.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseRepository courseRepo;

    @PostMapping
    public Course create(@RequestBody Course course) {
        return courseRepo.save(course);
    }

    @PutMapping("/{id}")
    public Course update(
            @PathVariable String id,
            @RequestBody Course updated
    ) {
        updated.setId(id);
        return courseRepo.save(updated);
    }

    @DeleteMapping("/{id}")
    public void disable(@PathVariable String id) {
        Course c = courseRepo.findById(id).orElseThrow();
        c.setActive(false);
        courseRepo.save(c);
    }

    @PutMapping("/{id}/price")
    public Course updatePrice(
            @PathVariable String id,
            @RequestParam int price
    ) {
        Course c = courseRepo.findById(id).orElseThrow();
        c.setPrice(price);
        return courseRepo.save(c);
    }
}
