package com.ja.course.service;

import com.ja.course.dto.CourseCardDto;
import com.ja.course.entity.Course;
import com.ja.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseQueryService {

    private final CourseRepository courseRepo;

    public List<CourseCardDto> getAllCourses() {
        return courseRepo.findAll()
                .stream()
                .filter(Course::isActive)
                .map(CourseCardDto::publicView)
                .toList();
    }
}

