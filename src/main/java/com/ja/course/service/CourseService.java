package com.ja.course.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.course.entity.Course;
import com.ja.course.player.CoursePlayerData;
import com.ja.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ObjectMapper mapper;

    public CoursePlayerData getCourseContent(String courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow();

        if (!course.isPublished()) {
            throw new RuntimeException("Course not published");
        }

        return mapper.convertValue(
                course.getPublishedContent(),
                CoursePlayerData.class
        );
    }
}
